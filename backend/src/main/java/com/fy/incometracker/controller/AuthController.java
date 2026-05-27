package com.fy.incometracker.controller;

import com.fy.incometracker.dto.LoginRequestDTO;
import com.fy.incometracker.dto.LoginResponseDTO;
import com.fy.incometracker.dto.RegisterRequestDTO;
import com.fy.incometracker.entity.User;
import com.fy.incometracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequestDTO request) {
        User user = authService.register(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = authService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}
