package com.zakneer.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    @NotBlank
    private String nickname;
    @NotBlank
    private String contrasenia;
    @NotBlank
    private String confirmarContrasenia;
}
