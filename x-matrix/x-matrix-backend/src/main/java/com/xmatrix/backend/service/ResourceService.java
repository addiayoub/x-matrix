package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.ResourceRequestDTO;
import com.xmatrix.backend.DTOs.ResourceResponseDTO;
import com.xmatrix.backend.entity.Company;
import com.xmatrix.backend.entity.Resource;
import com.xmatrix.backend.entity.User;
import com.xmatrix.backend.enums.Trend;
import com.xmatrix.backend.mappers.ResourceMapper;
import com.xmatrix.backend.repository.CompanyRepository;
import com.xmatrix.backend.repository.ResourceRepository;
import com.xmatrix.backend.repository.UserRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ResourceService(ResourceRepository resourceRepository, ResourceMapper resourceMapper, CompanyRepository companyRepository, UserRepository userRepository, UserService userService) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<ResourceResponseDTO> getAllResources() {
        return resourceRepository.findAllActiveAndOrderByCreatedAtDesc().stream()
                .map(resourceMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<ResourceResponseDTO> findAllActiveAndAndCompanyOrderByCreatedAtDesc(Long companyId) {
        return resourceRepository.findAllActiveAndAndCompanyOrderByCreatedAtDesc(companyId).stream()
                .map(resourceMapper::toDto)
                .collect(Collectors.toList());
    }


    public Optional<ResourceResponseDTO> getResourceById(Long id) {
        return resourceRepository.findById(id).map(resourceMapper::toDto);
    }


    @Transactional
    public ResourceResponseDTO createResource(ResourceRequestDTO requestDTO) throws Exception {
        User user = userService.getConnectedUser();

        if(user.getRole() == null || user.getRole().equals("SolutionOwner")){
            throw new Exception("Super admin can't create a resource");
        }

        Optional<Resource> optionalResource = resourceRepository.findByDepartmentAndDeletedIsFalse(requestDTO.getDepartment());

        Resource resource;
        if (optionalResource.isPresent()) {
            resource = optionalResource.get();

        } else {
            resource = new Resource();
            resource.setDepartment(requestDTO.getDepartment());
            resource.setActualProgress(0.0);
            resource.setTimelyProgress(0.0);


            Optional<Company> company = companyRepository.findById(user.getCompany().getId());

            company.ifPresentOrElse(resource::setCompany, () -> {
                throw new ExecutionException("Company not found");
            });
        }

        return resourceMapper.toDto(resourceRepository.save(resource));
    }

    public ResourceResponseDTO updateResource(Long id, ResourceRequestDTO resourceRequestDTO) {
        // Fetch the existing Resource entity
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));

        // Update the name if provided
        if (resourceRequestDTO.getDepartment() != null) {
            resource.setDepartment(resourceRequestDTO.getDepartment());
        }
        // Save the updated Resource entity
        Resource updatedResource = resourceRepository.save(resource);

        // Return the updated Resource as a DTO
        return resourceMapper.toDto(updatedResource);
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
}
