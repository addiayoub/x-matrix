package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.*;
import com.xmatrix.backend.entity.*;
import com.xmatrix.backend.mappers.*;
import com.xmatrix.backend.repository.UserRepository;
import com.xmatrix.backend.repository.XMatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class XMatrixService {
    @Autowired
    private XMatrixRepository xMatrixRepository;
    @Autowired
    private XMatrixMapper xMatrixMapper;
    @Autowired
    private SOMapper soMapper;
    @Autowired
    private AOMapper aoMapper;
    @Autowired
    private ITMapper itMapper;
    @Autowired
    private IPMapper ipMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private UserService userService;

    //@Transactional(readOnly = true)
    public List<XMatrixResponse> getAllXMatrices() {
        return xMatrixRepository.findAllActiveAndOrderByCreatedAtDesc().stream().map(xMatrixMapper::toDto).toList();
    }

    public XMatrixResponse getXMatrixById(Long id) {
        return xMatrixMapper.toDto(xMatrixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("XMatrix not found with id: " + id)));
    }

    public XMatrixResponse getXMatrixByUserConnected() {
        User user = userService.getConnectedUser();
        return xMatrixMapper.toDto(xMatrixRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("XMatrix not found with id: " + user.getId())));
    }
    public XMatrixResponse getXMatrixByUserId(Long id) {
        return xMatrixMapper.toDto(xMatrixRepository.findByUser_Id(id)
                .orElseThrow(() -> new RuntimeException("XMatrix not found with id: " + id)));
    }

    public XMatrixResponse createXMatrix(XMatrixRequest xMatrix) throws Exception {
        // Get the current authenticated user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Typically the username is stored in the token

        User user = userRepository.findByEmail(username).orElse(null);
//        if(xMatrixRepository.findByUser_Id(user.getId()) != null) {
//            throw new Exception("XMatrix already exists!");
//        }

        // Map the request to an entity
        XMatrix xMatrixEntity = xMatrixMapper.toEntity(xMatrix);
        xMatrixEntity.setUser(user);

        // Save the entity and return the response
        return xMatrixMapper.toDto(xMatrixRepository.save(xMatrixEntity));
    }

    public XMatrixResponse updateXMatrix(Long id, XMatrixRequestDTO xMatrixRequestDTO) {
        XMatrix xMatrix = xMatrixRepository.findById(id).get();
        xMatrix.setLabel(xMatrixRequestDTO.getLabel());
        return xMatrixMapper.toDto(xMatrixRepository.save(xMatrix));
    }

    public void deleteXMatrix(Long id) {
        XMatrix xMatrix = xMatrixRepository.findById(id).get();
        xMatrixRepository.delete(xMatrix);
    }


    public List<SOResponse> getAllSOsByXMatrix(@PathVariable Long id) {
        return xMatrixRepository.getAllSOsByXMatrix(id).stream().map(soMapper::toDto).toList();
    }

    public List<AOResponse> getAllAOsByXMatrix(@PathVariable Long id) {
        return xMatrixRepository.getAllAOsByXMatrix(id).stream().map(aoMapper::toDto).toList();
    }

    public List<IPResponse> getAllIPsByXMatrix(@PathVariable Long id) {
        return xMatrixRepository.getAllIPsByXMatrix(id).stream().map(ipMapper::toDto).toList();
    }

    public List<ITResponse> getAllITsByXMatrix(@PathVariable Long id) {
        return xMatrixRepository.getAllITsByXMatrix(id).stream().map(itMapper::toDto).toList();
    }

    public List<ResourceResponseDTO> getAllResourcessByXMatrix(@PathVariable Long id) {
        return xMatrixRepository.getAllResourcesByXMatrix(id).stream().map(resourceMapper::toDto).toList();
    }

    public List<XMatrixResponse> getMatricesUnderHierarchy() {
        User currentUser = userService.getConnectedUser();

        List<XMatrix> matrices = xMatrixRepository.findMatricesUnderUserHierarchy(currentUser.getId());



        return matrices.stream()
                .map(xMatrixMapper::toDto)
                .collect(Collectors.toList());
    }




}
