package com.zakneer.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String error;
    private Integer estado;
    private String mensaje;
    private LocalDateTime tiempo;
    private String ruta;
}
