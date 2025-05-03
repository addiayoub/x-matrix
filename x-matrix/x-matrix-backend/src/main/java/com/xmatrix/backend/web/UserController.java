package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.UserResponseDTO;
import com.xmatrix.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("my-infos")
    public ResponseEntity<UserResponseDTO> getUserByToekn() {
        return ResponseEntity.ok(userService.getUserByToken());
    }




    @GetMapping("hasXMatrix")
    public ResponseEntity<List<UserResponseDTO>> hasXMatrix() {
        return ResponseEntity.ok(userService.getAllUsersWithXMatrix());
    }

}
