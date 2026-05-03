package com.zakneer.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zakneer.backend.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Jackson ObjectMapper para convertir objetos a JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse error = ErrorResponse.builder()
                .estado(HttpServletResponse.SC_UNAUTHORIZED)
                .error("No autorizado")
                .mensaje("Token inexistente, inválido o expirado")
                .ruta(request.getRequestURI())
                .build();


        String json = objectMapper.writeValueAsString(error);
        response.getWriter().write(json);
}
}
