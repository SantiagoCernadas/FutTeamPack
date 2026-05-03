package com.zakneer.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "usuarioxequipo")
@IdClass(UsuarioEquipoId.class)
public class UsuarioXEquipoEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private EquipoEntity equipo;

    @NotNull
    private Integer cantidad;
}
