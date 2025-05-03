package com.xmatrix.backend.service;


import com.xmatrix.backend.DTOs.AuthenticationResponseDTO;
import com.xmatrix.backend.DTOs.LoginRequestDTO;
import com.xmatrix.backend.DTOs.RegisterRequestDTO;
import com.xmatrix.backend.entity.Company;
import com.xmatrix.backend.entity.Role;
import com.xmatrix.backend.entity.User;
import com.xmatrix.backend.mappers.RoleRepository;
import com.xmatrix.backend.repository.CompanyRepository;
import com.xmatrix.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    @Lazy
    private final AuthenticationManager authenticationManager;
    private final CompanyRepository companyRepository;

    public ResponseEntity<Object> register(RegisterRequestDTO request){
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if(existingUser.isPresent()){
            return ResponseEntity.badRequest().body("User Already Exist");
        }


        var user = User
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if(request.getRoleId() != null){
            Optional<Role> role = roleRepository.findById(request.getRoleId());
            if(role.isEmpty()) {
                return ResponseEntity.badRequest().body("Role Not Found");
            }
            user.setRole(role.get());
        }
        if(request.getParentId() != null){
            Optional<User> parentUser = userRepository.findById(request.getParentId());
            if(parentUser.isEmpty()){
                user.setParentUser(null);
            }else{
                user.setParentUser(parentUser.get());
            }
        }

        if(request.getCompanyId() != null){
            Optional<Company> company = companyRepository.findById(request.getCompanyId());
            if(company.isEmpty()){
                return ResponseEntity.badRequest().body("Company Not Found");
            }
            else{
                user.setCompany(company.get());
            }
        }
        User savedUser = userRepository.save(user);
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",savedUser.getUserName());
        claims.put("role",user.getRole().getName());
        var jwtToken = jwtService.generateToken(claims,user);


        return ResponseEntity.ok(AuthenticationResponseDTO
                .builder()
                .token(jwtToken)
                .build());
    }

    public AuthenticationResponseDTO login(LoginRequestDTO request){

        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            // Handle authentication failure, e.g., log the error or throw a custom exception
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        Map<String,Object> claims = new HashMap<>();
        claims.put("username",user.getUserName());
        claims.put("role",user.getRole().getName());


        var jwtToken = jwtService.generateToken(claims,user);

        return AuthenticationResponseDTO
                .builder()
                .token(jwtToken)
                .build();
    }

}
