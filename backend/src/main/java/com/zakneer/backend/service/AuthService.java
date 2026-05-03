package com.zakneer.backend.service;

import com.zakneer.backend.dto.LoginRequest;
import com.zakneer.backend.dto.LoginResponse;
import com.zakneer.backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    public LoginResponse getToken(Map<String, String> headers, LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getNickname(),
                        loginRequest.getContrasenia())
        );

        String token = jwtUtils.generarToken((UserDetails) authentication.getPrincipal());
        return new LoginResponse(token);
    }
}
