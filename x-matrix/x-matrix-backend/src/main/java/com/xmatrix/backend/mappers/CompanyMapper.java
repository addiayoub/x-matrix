package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.CompanyRequestDTO;
import com.xmatrix.backend.DTOs.CompanyResponseDTO;
import com.xmatrix.backend.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);


    Company toEntity(CompanyRequestDTO dto);

    CompanyResponseDTO toDto(Company company);
}
