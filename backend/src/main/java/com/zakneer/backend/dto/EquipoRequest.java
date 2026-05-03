package com.zakneer.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipoRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String tier;
    @NotBlank
    private String liga;
    @NotBlank
    private String imagen;
}
