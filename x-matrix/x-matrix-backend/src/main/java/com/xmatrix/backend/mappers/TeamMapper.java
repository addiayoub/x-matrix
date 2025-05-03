package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.TeamRequestDTO;
import com.xmatrix.backend.DTOs.TeamResponseDTO;
import com.xmatrix.backend.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);


    Team toEntity(TeamRequestDTO dto);

    TeamResponseDTO toDto(Team team);
}
