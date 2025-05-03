package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.IPResponse;
import com.xmatrix.backend.DTOs.ITRequest;
import com.xmatrix.backend.DTOs.ITResponse;
import com.xmatrix.backend.DTOs.SORequest;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.entity.IT;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ITMapper {
    ITMapper INSTANCE = Mappers.getMapper(ITMapper.class);


    IT toEntity(ITRequest dto);
    @Mapping(source = "ip.id", target = "ipId")
    @Mapping(source="resource.id",target="resourceId")
    ITResponse toDto(IT it);
}
