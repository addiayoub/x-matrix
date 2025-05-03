package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.SORequest;
import com.xmatrix.backend.DTOs.SOResponse;
import com.xmatrix.backend.DTOs.XMatrixRequest;
import com.xmatrix.backend.DTOs.XMatrixResponse;
import com.xmatrix.backend.entity.SO;
import com.xmatrix.backend.entity.XMatrix;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AOMapper.class})
public interface SOMapper {
    SOMapper INSTANCE = Mappers.getMapper(SOMapper.class);


    SO toEntity(SORequest dto);

    @Mapping(source = "aos", target = "aos")
    SOResponse toDto(SO so);
}
