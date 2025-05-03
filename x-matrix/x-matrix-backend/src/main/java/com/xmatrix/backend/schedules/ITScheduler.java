package com.xmatrix.backend.schedules;

import com.xmatrix.backend.entity.CustomLog;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.entity.Progress;
import com.xmatrix.backend.enums.Action;
import com.xmatrix.backend.enums.Trend;
import com.xmatrix.backend.repository.CustomLogRepository;
import com.xmatrix.backend.repository.IPRepository;
import com.xmatrix.backend.repository.ITRepository;
import com.xmatrix.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ITScheduler {
    @Autowired
    private ITRepository itRepository;
    @Autowired
    private CustomLogRepository customLogRepository;
    @Autowired
    private IPRepository ipRepository;

    public void scheduleAllITs() {
        List<IT> its = itRepository.findAll();
        for (IT it : its) {
            try {
                scheduleIT(it);
            } catch (Exception e) {
                // Instead of e.printStackTrace(), log the error properly
                e.printStackTrace(); // Or use a logger here: logger.error("Error scheduling IT", e);
            }
        }
    }
    private Double computeAverageAdvancement(List<IT> its) {
        return its.isEmpty() ? 0.0 : Math.ceil(its.stream().mapToDouble(IT::getAdvancement).average().orElse(0.0));
    }

    public void scheduleIT(IT it) {
        if (it.getStart() != null && it.getEnd() != null) {

            System.out.println("IP: "+it.getIp().getId());
            IP ip = ipRepository.findById(ipRepository.findById(it.getIp().getId()).get().getId()).get();
            ip.setAdvancement(computeAverageAdvancement(ip.getIts()));
            ipRepository.save(ip);

            // Calculate time spent and progress time
            Long timeSpent = Utils.calculatePeriodLength(it.getStart(),new Date());
            System.out.println("Time spent: " + timeSpent+" periodLength: "+it.getPeriodLength());
            Double progressTime = ((double)timeSpent / it.getPeriodLength()) * 100;

            // Set fields on IT entity
            it.setTimeSpent(timeSpent);
            it.setProgressTime(progressTime);
            it.setTrend(it.getAdvancement() < progressTime ? Trend.BEHIND : Trend.AHEAD);

            // Save IT entity
            IT saved = itRepository.save(it);

            // Log the custom log
            CustomLog customLog = CustomLog.builder()
                    .entityId(saved.getId())
                    .entityName("IT")
                    .updatedBy(0L) // You might want to replace with actual user ID
                    .start(saved.getStart())
                    .end(saved.getEnd())
                    .progressTime(saved.getProgressTime())
                    .advancement(saved.getAdvancement())
                    .periodLength(saved.getPeriodLength())
                    .timeSpent(saved.getTimeSpent())
                    .trend(saved.getTrend())
                    .action(Action.CREATE) // Ensure this action is appropriate
                    .build();

            customLogRepository.save(customLog);
        } else {
            // Handle missing start/end dates
            System.out.println("Missing start or end date for IT entity: " + it.getId());
        }
    }
}
