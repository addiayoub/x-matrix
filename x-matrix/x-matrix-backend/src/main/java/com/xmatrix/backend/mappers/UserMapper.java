package com.xmatrix.backend.mappers;



import com.xmatrix.backend.DTOs.UserResponseDTO;
import com.xmatrix.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {CompanyMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "parentUser.id", target = "parentId")
    @Mapping(source = "username", target = "username")
    UserResponseDTO toDto(User user);
    User toEntity(UserResponseDTO userDto);
}

