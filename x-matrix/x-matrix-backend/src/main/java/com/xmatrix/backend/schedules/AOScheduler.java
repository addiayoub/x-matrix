package com.xmatrix.backend.schedules;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.CustomLog;
import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.entity.SO;
import com.xmatrix.backend.enums.Action;
import com.xmatrix.backend.enums.Trend;
import com.xmatrix.backend.repository.AORepository;
import com.xmatrix.backend.repository.CustomLogRepository;
import com.xmatrix.backend.repository.SORepository;
import com.xmatrix.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AOScheduler {
    @Autowired
    private AORepository aoRepository;
    @Autowired
    private CustomLogRepository customLogRepository;
    @Autowired
    private SORepository soRepository;

    public void scheduleAllAOs() {
        List<AO> aos = aoRepository.findAll();
        for (AO ao : aos) {
            try {
                scheduleAO(ao);
            } catch (Exception e) {
                // Instead of e.printStackTrace(), log the error properly
                e.printStackTrace(); // Or use a logger here: logger.error("Error scheduling IT", e);
            }
        }
    }

    private Double computeAverageAdvancement(List<AO> aos) {

        return aos.isEmpty() ? 0.0 : Math.ceil(aos.stream().mapToDouble(AO::getAdvancement).average().orElse(0.0));
    }

    public void scheduleAO(AO ao) {
        if (ao.getStart() != null && ao.getEnd() != null) {
            SO so = soRepository.findById(soRepository.findById(ao.getSo().getId()).get().getId()).get();
            so.setAdvancement(computeAverageAdvancement(so.getAos()));
            soRepository.save(so);

            System.out.println("length: "+so.getAos().size());



            // Calculate time spent and progress time
            Long timeSpent = Utils.calculatePeriodLength(ao.getStart(),new Date());
            System.out.println("Time spent: " + timeSpent+" periodLength: "+ao.getPeriodLength());
            Double progressTime = ((double)timeSpent / ao.getPeriodLength()) * 100;

            // Set fields on IT entity
            ao.setTimeSpent(timeSpent);
            ao.setProgressTime(progressTime);
            if(ao.getAdvancement() != null) {
                ao.setTrend(ao.getAdvancement() < progressTime ? Trend.BEHIND : Trend.AHEAD);

            }

            // Save IT entity
            AO saved = aoRepository.save(ao);

            // Log the custom log
            CustomLog customLog = CustomLog.builder()
                    .entityId(saved.getId())
                    .entityName("AO")
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
            System.out.println("Missing start or end date for AO entity: " + ao.getId());
        }
    }

}
