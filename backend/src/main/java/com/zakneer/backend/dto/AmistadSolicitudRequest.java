package com.zakneer.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmistadSolicitudRequest {
    private String usuarioAmistad;
    private boolean respuesta;
}
