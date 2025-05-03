package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.CompanyRequestDTO;
import com.xmatrix.backend.DTOs.CompanyResponseDTO;
import com.xmatrix.backend.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<CompanyResponseDTO> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable Long id) {
        Optional<CompanyResponseDTO> company = companyService.getCompanyById(id);
        return company.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CompanyResponseDTO createCompany(@RequestBody CompanyRequestDTO requestDTO) {
        return companyService.createCompany(requestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyRequestDTO requestDTO) {
        return ResponseEntity.ok(companyService.updateCompany(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteCompany(@PathVariable Long id) {
        companyService.softDeleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restoreCompany(@PathVariable Long id) {
        companyService.restoreCompany(id);
        return ResponseEntity.noContent().build();
    }
}
