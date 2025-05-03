package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.SORequest;
import com.xmatrix.backend.DTOs.SOResponse;
import com.xmatrix.backend.DTOs.SOUpdateDTO;
import com.xmatrix.backend.DTOs.XMatrixResponse;
import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.SO;
import com.xmatrix.backend.entity.XMatrix;
import com.xmatrix.backend.mappers.SOMapper;
import com.xmatrix.backend.repository.SORepository;
import com.xmatrix.backend.repository.XMatrixRepository;
import com.xmatrix.backend.schedules.AOScheduler;
import com.xmatrix.backend.schedules.SOScheduler;
import com.xmatrix.backend.schedules.Scheduler;
import com.xmatrix.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SOService {

    @Autowired
    private SORepository soRepository;

    @Autowired
    private XMatrixService xMatrixService; // Assuming you have a service for XMatrix
    @Autowired
    private XMatrixRepository xMatrixRepository;
    @Autowired
    private SOMapper soMapper;
    @Autowired
    private SOScheduler soScheduler;
    @Autowired
    private Scheduler scheduler;

    @Transactional
    public void deleteAllByXMatrixId(Long xMatrixId) {
        soRepository.deleteByXMatrixId(xMatrixId);
    }


    public List<SOResponse> getAllSOs() {
        return soRepository.findAllActiveAndOrderByCreatedAtDesc().stream().map(soMapper::toDto).toList();
    }

    public SOResponse getSOById(Long id) {
        SO so = soRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SO not found with id: " + id));
        return soMapper.toDto(so);
    }
    @Transactional
    public SOResponse createSO(SORequest soRequest, Long xMatrixId) {
        XMatrix xMatrix = xMatrixRepository.findById(xMatrixId)
                .orElseThrow(() -> new RuntimeException("XMatrix not found with id: " + xMatrixId));

//        if(soRepository.findByLabel(soRequest.getLabel()) != null){
//            throw new RuntimeException("SO already exists with label: " + soRequest.getLabel());
//        }
        System.out.println("create SO: " + soRequest.getEnd());
        SO so = soMapper.toEntity(soRequest);
        so.setXMatrix(xMatrix);


        // Ensure start and end dates are correctly ordered for period length calculation
        so.setPeriodLength(Utils.calculatePeriodLength(soRequest.getStart(), soRequest.getEnd())); // Corrected date order


        SO saved = soRepository.save(so);

        soScheduler.scheduleSO(saved);

        scheduler.run();

        return soMapper.toDto(saved);
    }

    @Transactional
    public SOResponse updateSO(Long id, SOUpdateDTO soUpdateDTO) {
        // Fetch the existing SO entity
        SO so = soRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SO not found with id: " + id));

        // Update the label if provided
        if (soUpdateDTO.getLabel() != null) {
            so.setLabel(soUpdateDTO.getLabel());
        }

        // Update the start and end dates if provided
        if (soUpdateDTO.getStart() != null && soUpdateDTO.getEnd() != null) {
            so.setStart(soUpdateDTO.getStart());
            so.setEnd(soUpdateDTO.getEnd());
            // Recalculate the period length
            so.setPeriodLength(Utils.calculatePeriodLength(soUpdateDTO.getStart(), soUpdateDTO.getEnd()));
        } else if (soUpdateDTO.getStart() != null || soUpdateDTO.getEnd() != null) {
            // If only one of the dates is provided, throw an exception
            throw new IllegalArgumentException("Both start and end dates must be provided together");
        }

        // Save the updated SO entity
        SO updatedSO = soRepository.save(so);

        // Reschedule the SO task if the dates have changed
        if (soUpdateDTO.getStart() != null || soUpdateDTO.getEnd() != null) {
            soScheduler.scheduleSO(updatedSO);
        }

        // Return the updated SO as a DTO
        return soMapper.toDto(updatedSO);
    }

    private Double computeAverageAdvancement(List<SO> sos) {

        return sos.isEmpty() ? 0.0 : sos.stream().mapToDouble(SO::getAdvancement).average().orElse(0.0);
    }



    public void deleteSO(Long id) {
        SO so = soRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SO not found with id: " + id));
        soRepository.delete(so);
    }

    public List<AO> getAOsBySOId(Long soId) {
        SO so = soRepository.findById(soId)
                .orElseThrow(() -> new RuntimeException("SO not found with id: " + soId));
        return so.getAos();
    }
}
