package com.xmatrix.backend.schedules;

import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.entity.Resource;
import com.xmatrix.backend.entity.SO;
import com.xmatrix.backend.enums.Trend;
import com.xmatrix.backend.repository.ITRepository;
import com.xmatrix.backend.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceScheduler {

    @Autowired
    private ITRepository itRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    public void scheduleAllResources(){
        List<Resource> resources = resourceRepository.findAll();
        for (Resource resource : resources) {
            try {
                scheduleResource(resource);
            } catch (Exception e) {
                // Instead of e.printStackTrace(), log the error properly
                e.printStackTrace(); // Or use a logger here: logger.error("Error scheduling IT", e);
            }
        }
    }
    private Double computeAverageAdvancement(List<IT> its) {

        return its.isEmpty() ? 0.0 : Math.ceil(its.stream().mapToDouble(IT::getAdvancement).average().orElse(0.0));
    }
    private Double computeAverageProgressTime(List<IT> its) {
        return its.isEmpty() ? 0.0 : its.stream().mapToDouble(IT::getProgressTime).average().orElse(0.0);
    }

    public void scheduleResource(Resource resource){
        List<IT> its = itRepository.findAllByResource_Id(resource.getId());

        resource.setActualProgress(computeAverageAdvancement(its));
        resource.setTimelyProgress(computeAverageProgressTime(its));

        if(resource.getActualProgress() < resource.getTimelyProgress()){
            resource.setTrend(Trend.BEHIND);
        }else{
            resource.setTrend(Trend.AHEAD);
        }

        resourceRepository.save(resource);

    }
}
