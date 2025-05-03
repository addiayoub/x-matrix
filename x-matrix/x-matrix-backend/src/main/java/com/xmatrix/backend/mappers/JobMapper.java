package com.xmatrix.backend.mappers;

import com.xmatrix.backend.DTOs.JobRequestDTO;
import com.xmatrix.backend.DTOs.JobResponseDTO;
import com.xmatrix.backend.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    JobResponseDTO toDto(Job job);
    Job toEntity(JobRequestDTO dto);
}
