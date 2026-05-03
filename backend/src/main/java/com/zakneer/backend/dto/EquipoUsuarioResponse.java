package com.zakneer.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipoUsuarioResponse {
    private String nombre;
    private PaisResponse pais;
    private LigaResponse liga;
    private String tier;
    private String imagen;
    private int cantidad;
}
