package com.zakneer.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipoResponse {
    private String nombre;
    private String tier;
    private String liga;
    private String pais;
    private String imagen;
}
