package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.DirectionRequestDTO;
import com.xmatrix.backend.DTOs.DirectionResponseDTO;
import com.xmatrix.backend.entity.Direction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DirectionMapper {
    DirectionMapper INSTANCE = Mappers.getMapper(DirectionMapper.class);

    DirectionResponseDTO toDto(Direction direction);
    Direction toEntity(DirectionRequestDTO dto);
}
