package com.zakneer.backend.controller;

import com.zakneer.backend.dto.LoginRequest;
import com.zakneer.backend.dto.LoginResponse;
import com.zakneer.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestHeader Map<String,String> headers, @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.getToken(headers,loginRequest));
    }
}
