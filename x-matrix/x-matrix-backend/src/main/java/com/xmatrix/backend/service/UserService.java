package com.xmatrix.backend.service;


import com.xmatrix.backend.DTOs.HirarchcalResponse;
import com.xmatrix.backend.DTOs.UserResponseDTO;
import com.xmatrix.backend.entity.User;
import com.xmatrix.backend.mappers.CompanyMapper;
import com.xmatrix.backend.mappers.RoleRepository;
import com.xmatrix.backend.mappers.UserMapper;
import com.xmatrix.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyMapper companyMapper;
    private final UserMapper userMapper;

    public User getConnectedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Typically the username is stored in the token

        return userRepository.findByEmail(username).orElse(null);
    }

    public UserResponseDTO getUserByToken(){
        return userMapper.toDto(getConnectedUser());
    }



    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAllActiveAndOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getAllUsersWithXMatrix() {

        List<User> users = userRepository.findAllUsersWithXMatrixAndActiveAndOrderByCreatedAtDesc();
        for (User user : users) {
            System.out.println(user.getId());
        }
        return userRepository.findAllUsersWithXMatrixAndActiveAndOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setCompany(companyMapper.toDto(user.getCompany()));
        if(user.getRole() != null) {
            dto.setRoleName(user.getRole().getName());
        }
        dto.setParentId(user.getParentUser() != null ? user.getParentUser().getId() : null);

        return dto;
    }

//    public List<HirarchcalResponse> hierarchicalOrganizationChart(){
//        List<>
//    }

}
