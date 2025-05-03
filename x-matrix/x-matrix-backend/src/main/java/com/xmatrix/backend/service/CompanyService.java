package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.CompanyRequestDTO;
import com.xmatrix.backend.DTOs.CompanyResponseDTO;
import com.xmatrix.backend.entity.Company;
import com.xmatrix.backend.mappers.CompanyMapper;
import com.xmatrix.backend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<CompanyResponseDTO> getAllCompanies() {
        List<Company> companies = companyRepository.findAllActiveAndOrderByCreatedAtDesc();
        return companies.stream().map(CompanyMapper.INSTANCE::toDto).toList();
    }

    public Optional<CompanyResponseDTO> getCompanyById(Long id) {
        return companyRepository.findById(id)
                .filter(company -> !company.getDeleted()) // Ensure soft deleted items are ignored
                .map(CompanyMapper.INSTANCE::toDto);
    }

    public CompanyResponseDTO createCompany(CompanyRequestDTO requestDTO) {
        Company company = CompanyMapper.INSTANCE.toEntity(requestDTO);
        company.setDeleted(false); // Ensure new entries are not marked as deleted
        company = companyRepository.save(company);
        return CompanyMapper.INSTANCE.toDto(company);
    }

    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO requestDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setName(requestDTO.getName());
        company.setLocation(requestDTO.getLocation());
        company.setLogo(requestDTO.getLogo());
        company = companyRepository.save(company);
        return CompanyMapper.INSTANCE.toDto(company);
    }

    public void softDeleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setDeleted(true);
        companyRepository.save(company);
    }

    public void restoreCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setDeleted(false);
        companyRepository.save(company);
    }
}
