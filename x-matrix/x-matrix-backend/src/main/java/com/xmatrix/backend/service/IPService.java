package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.IPRequest;
import com.xmatrix.backend.DTOs.IPResponse;
import com.xmatrix.backend.DTOs.IPUpdateDTO;
import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.mappers.IPMapper;
import com.xmatrix.backend.repository.AORepository;
import com.xmatrix.backend.repository.IPRepository;
import com.xmatrix.backend.schedules.AOScheduler;
import com.xmatrix.backend.schedules.IPScheduler;
import com.xmatrix.backend.schedules.Scheduler;
import com.xmatrix.backend.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IPService {
    @Autowired
    private IPRepository ipRepository;

    @Autowired
    @Lazy
    private AOService aoService;
    @Autowired
    private IPMapper ipMapper;// Assuming you have a service for AO
    @Autowired
    private AORepository aoRepository;
    @Autowired
    private IPScheduler ipScheduler;
    @Autowired
    private ITService itService; // Assuming you have a service for IT
    @Autowired
    private Scheduler scheduler;

    public List<IPResponse> getAllIPs() {
        return ipRepository.findAllActiveAndOrderByCreatedAtDesc().stream().map(ipMapper::toDto).collect(Collectors.toList());
    }

    public IPResponse getIPById(Long id) {
        IP ip = ipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IP not found with id: " + id));
        return ipMapper.toDto(ip);
    }
    @Transactional
    public void deleteAllByXMatrixId(Long xMatrixId) {
        ipRepository.deleteByXMatrixId(xMatrixId);
    }

    @Transactional
    public IPResponse createIP(IPRequest ipRequest, Long aoId) {
        AO ao = aoRepository.findById(aoId)
                .orElseThrow(() -> new EntityNotFoundException("AO not found with id: " + aoId)); // Handle missing IP entity

      // Fetch the AO entity
        IP ip = ipMapper.toEntity(ipRequest);
        ip.setAo(ao); // Set the AO for the IP

        // Ensure start and end dates are correctly ordered for period length calculation
        ip.setPeriodLength(Utils.calculatePeriodLength(ipRequest.getStart(), ipRequest.getEnd())); // Corrected date order


        IP savedIP = ipRepository.save(ip);

        ao.getIps().add(savedIP);
//        ao.setAdvancement(computeAverageAdvancement(ao.getIps()));
//       aoRepository.save(ao);

        // Schedule the IP task
        ipScheduler.scheduleIP(savedIP);

        scheduler.run();


        return ipMapper.toDto(savedIP);
    }

    @Transactional
    public IPResponse updateIP(Long id, IPUpdateDTO ipUpdateDTO) {
        IP ip = ipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IP not found with id: " + id));

        if (ipUpdateDTO.getLabel() != null) {
            ip.setLabel(ipUpdateDTO.getLabel());
        }

        if (ipUpdateDTO.getStart() != null && ipUpdateDTO.getEnd() != null) {
            ip.setStart(ipUpdateDTO.getStart());
            ip.setEnd(ipUpdateDTO.getEnd());
            ip.setPeriodLength(Utils.calculatePeriodLength(ipUpdateDTO.getStart(), ipUpdateDTO.getEnd()));
        } else if (ipUpdateDTO.getStart() != null || ipUpdateDTO.getEnd() != null) {
            throw new IllegalArgumentException("Both start and end dates must be provided together");
        }

        IP updatedIP = ipRepository.save(ip);

        if (ipUpdateDTO.getStart() != null || ipUpdateDTO.getEnd() != null) {
            ipScheduler.scheduleIP(updatedIP);
        }

        return ipMapper.toDto(updatedIP);
    }



    public void deleteIP(Long id) {
        IP ip = ipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IP not found with id: " + id));
        ipRepository.delete(ip);
    }



}
