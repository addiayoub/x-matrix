package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.AORequest;
import com.xmatrix.backend.DTOs.AOResponse;
import com.xmatrix.backend.DTOs.AOUpdateDTO;
import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.entity.SO;
import com.xmatrix.backend.mappers.AOMapper;
import com.xmatrix.backend.repository.AORepository;
import com.xmatrix.backend.repository.SORepository;
import com.xmatrix.backend.schedules.AOScheduler;
import com.xmatrix.backend.schedules.Scheduler;
import com.xmatrix.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AOService {

    @Autowired
    private AORepository aoRepository;
    @Autowired
    private SORepository soRepository;
    @Autowired
    private AOMapper aoMapper;
    @Autowired
    private AOScheduler aoScheduler;

    @Autowired
    @Lazy
    private IPService ipService;
    @Autowired
    private Scheduler scheduler;

    public List<AOResponse> getAllAOs() {
        return aoRepository.findAllActiveAndOrderByCreatedAtDesc().stream().map(aoMapper::toDto).collect(Collectors.toList());
    }

    public AOResponse getAOById(Long id) {
        AO ao = aoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AO not found with id: " + id));
        return aoMapper.toDto(ao);
    }

    @Transactional
    public void deleteAllByXMatrixId(Long xMatrixId) {
        aoRepository.deleteByXMatrixId(xMatrixId);
    }

    @Transactional
    public AOResponse createAO(AORequest aoRequest, Long soId) {
        SO so = soRepository.findById(soId)
                .orElseThrow(() -> new RuntimeException("SO not found with id: " + soId));

//        if(aoRepository.findByLabel(aoRequest.getLabel()) == null){
//            throw new RuntimeException("SO already exists with label: " + aoRequest.getLabel());
//        }

        AO ao = aoMapper.toEntity(aoRequest);
        ao.setSo(so);

        // Ensure start and end dates are correctly ordered for period length calculation
        ao.setPeriodLength(Utils.calculatePeriodLength(aoRequest.getStart(), aoRequest.getEnd())); // Corrected date order

        AO savedAo = aoRepository.save(ao);

        so.getAos().add(savedAo);


        aoScheduler.scheduleAO(savedAo);

        scheduler.run();

        return aoMapper.toDto(savedAo);
    }

    private Double computeAverageAdvancement(List<AO> aos) {

        return aos.isEmpty() ? 0.0 : aos.stream().mapToDouble(AO::getAdvancement).average().orElse(0.0);
    }

    @Transactional
    public AOResponse updateAO(Long id, AOUpdateDTO aoUpdateDTO) {
        AO ao = aoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AO not found with id: " + id));

        // Update the label if provided
        if (aoUpdateDTO.getLabel() != null) {
            ao.setLabel(aoUpdateDTO.getLabel());
        }

        // Update the start and end dates if provided
        if (aoUpdateDTO.getStart() != null && aoUpdateDTO.getEnd() != null) {
            ao.setStart(aoUpdateDTO.getStart());
            ao.setEnd(aoUpdateDTO.getEnd());
            // Recalculate the period length if dates are updated
            ao.setPeriodLength(Utils.calculatePeriodLength(aoUpdateDTO.getStart(), aoUpdateDTO.getEnd()));
        }

        // Save the updated entity
        AO updatedAO = aoRepository.save(ao);

        // Optionally, reschedule the AO if the dates have changed
        aoScheduler.scheduleAO(updatedAO);


        return aoMapper.toDto(updatedAO);
    }

    public void deleteAO(Long id) {
        AO ao = aoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AO not found with id: " + id));
        aoRepository.delete(ao);
    }
}
