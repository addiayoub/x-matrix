package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.ResourceRequestDTO;
import com.xmatrix.backend.DTOs.ResourceResponseDTO;
import com.xmatrix.backend.entity.Resource;
import com.xmatrix.backend.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<List<ResourceResponseDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @GetMapping("company/{companyId}")
    public ResponseEntity<List<ResourceResponseDTO>> findAllActiveAndAndCompanyOrderByCreatedAtDesc(@PathVariable Long companyId) {
        return ResponseEntity.ok(resourceService.findAllActiveAndAndCompanyOrderByCreatedAtDesc(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> getResourceById(@PathVariable Long id) {
        return resourceService.getResourceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResourceResponseDTO> createResource(@RequestBody @Valid ResourceRequestDTO requestDTO) throws Exception {
        ResourceResponseDTO responseDTO = resourceService.createResource(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> updateResource(
            @PathVariable Long id,
            @RequestBody @Valid ResourceRequestDTO requestDTO) {
        try {
            ResourceResponseDTO updatedResource = resourceService.updateResource(id, requestDTO);
            return ResponseEntity.ok(updatedResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}
