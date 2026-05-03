package com.zakneer.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "sobresabiertos")
@IdClass(EstadoSobreId.class)
public class SobreEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UsuarioEntity usuario;

    @Id
    @Enumerated(EnumType.STRING)
    private TipoSobre tipoSobre;

    private Instant horaApertura;
}
