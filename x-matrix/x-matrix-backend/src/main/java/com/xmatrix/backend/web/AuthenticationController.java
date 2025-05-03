package com.xmatrix.backend.web;



import com.xmatrix.backend.DTOs.AuthenticationResponseDTO;
import com.xmatrix.backend.DTOs.LoginRequestDTO;
import com.xmatrix.backend.DTOs.RegisterRequestDTO;
import com.xmatrix.backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO request){
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authenticationService.login(request));

    }

}
