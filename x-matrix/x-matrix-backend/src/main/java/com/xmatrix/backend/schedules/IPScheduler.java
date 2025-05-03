package com.xmatrix.backend.schedules;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.CustomLog;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.enums.Action;
import com.xmatrix.backend.enums.Trend;
import com.xmatrix.backend.repository.AORepository;
import com.xmatrix.backend.repository.CustomLogRepository;
import com.xmatrix.backend.repository.IPRepository;
import com.xmatrix.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class IPScheduler {
    @Autowired
    private IPRepository ipRepository;
    @Autowired
    private CustomLogRepository customLogRepository;
    @Autowired
    private AORepository aoRepository;

    public void scheduleAllIPs() {
        List<IP> ips = ipRepository.findAll();
        for (IP ip : ips) {
            try {
                scheduleIP(ip);
            } catch (Exception e) {
                // Instead of e.printStackTrace(), log the error properly
                e.printStackTrace(); // Or use a logger here: logger.error("Error scheduling IT", e);
            }
        }
    }
    private Double computeAverageAdvancement(List<IP> ips) {

        return ips.isEmpty() ? 0.0 : Math.ceil(ips.stream().mapToDouble(IP::getAdvancement).average().orElse(0.0));
    }
    public void scheduleIP(IP ip) {
        if (ip.getStart() != null && ip.getEnd() != null) {

            AO ao = aoRepository.findById(aoRepository.findById(ip.getAo().getId()).get().getId()).get();
            ao.setAdvancement(computeAverageAdvancement(ao.getIps()));
            aoRepository.save(ao);

            // Calculate time spent and progress time
            Long timeSpent = Utils.calculatePeriodLength(ip.getStart(),new Date());
            System.out.println("Time spent: " + timeSpent+" periodLength: "+ip.getPeriodLength());
            Double progressTime = ((double)timeSpent / ip.getPeriodLength()) * 100;

            // Set fields on IT entity
            ip.setTimeSpent(timeSpent);
            ip.setProgressTime(progressTime);
            if(ip.getAdvancement() != null) {
                ip.setTrend(ip.getAdvancement() < progressTime ? Trend.BEHIND : Trend.AHEAD);
            }
            // Save IP entity
            IP saved = ipRepository.save(ip);

            // Log the custom log
            CustomLog customLog = CustomLog.builder()
                    .entityId(saved.getId())
                    .entityName("IP")
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
            System.out.println("Missing start or end date for IP entity: " + ip.getId());
        }
    }
}
