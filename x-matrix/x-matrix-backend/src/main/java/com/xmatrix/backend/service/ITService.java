package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.ITRequest;
import com.xmatrix.backend.DTOs.ITResponse;
import com.xmatrix.backend.DTOs.ITUpdateDTO;
import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.entity.Resource;
import com.xmatrix.backend.mappers.ITMapper;
import com.xmatrix.backend.repository.IPRepository;
import com.xmatrix.backend.repository.ITRepository;
import com.xmatrix.backend.repository.ResourceRepository;
import com.xmatrix.backend.schedules.ITScheduler;
import com.xmatrix.backend.schedules.Scheduler;
import com.xmatrix.backend.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ITService {

    @Autowired
    private ITRepository itRepository;
    @Autowired
    private IPRepository ipRepository;
    @Autowired
    private ITMapper itMapper;
    @Autowired
    private ITScheduler itScheduler;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ResourceRepository resourceRepository;

    public List<ITResponse> getAllITs() {
        return itRepository.findAllActiveAndOrderByCreatedAtDesc().stream().map(itMapper::toDto).collect(Collectors.toList());
    }

    public ITResponse getITById(Long id) {
        return itMapper.toDto(itRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IT not found with id: " + id)));
    }
    @Transactional
    public void deleteAllByXMatrixId(Long xMatrixId) {
        itRepository.deleteByXMatrixId(xMatrixId);
    }
    @Transactional
    public ITResponse createIT(ITRequest itRequest, Long ipId) {
        IP ip = ipRepository.findById(ipId)
                .orElseThrow(() -> new EntityNotFoundException("IP not found with id: " + ipId));

        IT it = itMapper.toEntity(itRequest);
        it.setIp(ip);

        it.setPeriodLength(Utils.calculatePeriodLength(itRequest.getStart(), itRequest.getEnd()));

        Resource resource = resourceRepository.findById(itRequest.getResourceId()).get();
        it.setResource(resource);
        IT savedIT = itRepository.save(it);


        // Explicitly add IT to IP before saving
        ip.getIts().add(savedIT);
//        ip.setAdvancement(computeAverageAdvancement(ip.getIts()));
//        ipRepository.save(ip);
//
        itScheduler.scheduleIT(savedIT);

        scheduler.run();

        return itMapper.toDto(savedIT);
    }

    @Transactional
    public ITResponse updateIT(Long id, ITUpdateDTO itUpdateDTO) {
        // Fetch the existing IT entity
        IT it = itRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IT not found with id: " + id));

        // Update the label if provided
        if (itUpdateDTO.getLabel() != null) {
            it.setLabel(itUpdateDTO.getLabel());
        }

        // Update the start and end dates if provided
        if (itUpdateDTO.getStart() != null && itUpdateDTO.getEnd() != null) {
            it.setStart(itUpdateDTO.getStart());
            it.setEnd(itUpdateDTO.getEnd());
            // Recalculate the period length
            it.setPeriodLength(Utils.calculatePeriodLength(itUpdateDTO.getStart(), itUpdateDTO.getEnd()));
        }

        // Update the associated resource if resourceId is provided
        if (itUpdateDTO.getResourceId() != null) {
            Resource resource = resourceRepository.findById(itUpdateDTO.getResourceId())
                    .orElseThrow(() -> new EntityNotFoundException("Resource not found with id: " + itUpdateDTO.getResourceId()));
            it.setResource(resource);
        }

        if(itUpdateDTO.getAdvancement() != null) {
            it.setAdvancement(itUpdateDTO.getAdvancement());
        }

        // Save the updated IT entity
        IT updatedIT = itRepository.save(it);

        // Reschedule the IT task if the dates have changed
        if (itUpdateDTO.getStart() != null || itUpdateDTO.getEnd() != null) {
            itScheduler.scheduleIT(updatedIT);
        }

        // Return the updated IT as a DTO
        return itMapper.toDto(updatedIT);
    }



//    public ITResponse updateIT(Long id, IT itDetails) {
//        IT it = getITById(id);
//        it.setLabel(itDetails.getLabel()); // Update fields as needed
//        return itRepository.save(it);
//    }

    public void deleteIT(Long id) {
        IT it = itRepository.findById(id).get();
        itRepository.delete(it);
    }
}
