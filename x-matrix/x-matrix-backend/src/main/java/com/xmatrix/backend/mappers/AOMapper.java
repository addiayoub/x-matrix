package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.AORequest;
import com.xmatrix.backend.DTOs.AOResponse;
import com.xmatrix.backend.DTOs.IPResponse;
import com.xmatrix.backend.DTOs.ITRequest;
import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.IP;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {IPMapper.class})
public interface AOMapper {
    AOMapper INSTANCE = Mappers.getMapper(AOMapper.class);


    AO toEntity(AORequest dto);

    @Mapping(source = "ips", target = "ips") // Properly map IPs
    @Mapping(source = "so.id", target = "soId") // Set only the SO ID to avoid recursion
    AOResponse toDto(AO ao);

}
