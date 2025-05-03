package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.*;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.entity.SO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {AOMapper.class,IPMapper.class,ITMapper.class})
public interface IPMapper {
    IPMapper INSTANCE = Mappers.getMapper(IPMapper.class);


    IP toEntity(IPRequest dto);
    @Mapping(source = "its", target = "its")
    @Mapping(source = "ao.id", target = "aoId")
    IPResponse toDto(IP ip);
}

