package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.XMatrixRequest;
import com.xmatrix.backend.DTOs.XMatrixResponse;
import com.xmatrix.backend.entity.User;
import com.xmatrix.backend.entity.XMatrix;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {SOMapper.class,AOMapper.class,IPMapper.class,ITMapper.class,UserMapper.class})
public interface XMatrixMapper {
    XMatrixMapper INSTANCE = Mappers.getMapper(XMatrixMapper.class);

    XMatrix toEntity(XMatrixRequest dto);
    @Mapping(source = "sos", target = "sos")
    XMatrixResponse toDto(XMatrix xMatrix);
}

