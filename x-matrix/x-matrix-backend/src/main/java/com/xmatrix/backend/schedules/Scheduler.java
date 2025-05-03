package com.xmatrix.backend.schedules;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final AOScheduler aoScheduler;
    private final IPScheduler ipScheduler;
    private final ITScheduler itScheduler;
    private final SOScheduler soScheduler;
    private final ResourceScheduler resourceScheduler;

    public void run(){

        itScheduler.scheduleAllITs();
        resourceScheduler.scheduleAllResources();
        ipScheduler.scheduleAllIPs();
        aoScheduler.scheduleAllAOs();
        soScheduler.scheduleAllSOs();
    }
}
