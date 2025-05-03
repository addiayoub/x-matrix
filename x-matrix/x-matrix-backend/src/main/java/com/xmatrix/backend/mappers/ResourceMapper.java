package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.ResourceRequestDTO;
import com.xmatrix.backend.DTOs.ResourceResponseDTO;
import com.xmatrix.backend.entity.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);


    Resource toEntity(ResourceRequestDTO dto);
    @Mapping(source = "company.id", target = "companyId")
    ResourceResponseDTO toDto(Resource resource);
}
