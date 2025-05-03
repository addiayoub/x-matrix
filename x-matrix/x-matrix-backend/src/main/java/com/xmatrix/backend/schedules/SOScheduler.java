package com.xmatrix.backend.schedules;

import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.CustomLog;
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
public class SOScheduler {
    @Autowired
    private SORepository soRepository;
    @Autowired
    private CustomLogRepository customLogRepository;

    public void scheduleAllSOs() {
        List<SO> sos = soRepository.findAll();
        for (SO so : sos) {
            try {
                scheduleSO(so);
            } catch (Exception e) {
                // Instead of e.printStackTrace(), log the error properly
                e.printStackTrace(); // Or use a logger here: logger.error("Error scheduling IT", e);
            }
        }
    }

    public void scheduleSO(SO so) {
        if (so.getStart() != null && so.getEnd() != null) {
            // Calculate time spent and progress time
            Long timeSpent = Utils.calculatePeriodLength(so.getStart(),new Date());
            System.out.println("Time spent: " + timeSpent+" periodLength: "+so.getPeriodLength());
            Double progressTime = ((double)timeSpent / so.getPeriodLength()) * 100;

            // Set fields on IT entity
            so.setTimeSpent(timeSpent);
            so.setProgressTime(progressTime);
            if(so.getAdvancement() != null) {
                so.setTrend(so.getAdvancement() < progressTime ? Trend.BEHIND : Trend.AHEAD);
            }

            // Save IT entity
            SO saved = soRepository.save(so);

            // Log the custom log
            CustomLog customLog = CustomLog.builder()
                    .entityId(saved.getId())
                    .entityName("SO")
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
            System.out.println("Missing start or end date for SO entity: " + so.getId());
        }
    }
}
