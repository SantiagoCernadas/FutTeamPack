package com.zakneer.backend.dto;

import com.zakneer.backend.entity.TipoSobre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SobreResponse {
    private TipoSobre tipoSobre;
    private Long segundosRestantes;
    private String tiempoRestanteFormateado;
    private boolean disponible;
}
