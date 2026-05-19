package com.zakneer.backend.entity;

import jakarta.persistence.*;
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
@Entity(name = "amistad")
public class AmistadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_envia")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UsuarioEntity usuarioEnvia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_recibe")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UsuarioEntity usuarioRecibe;

    @Enumerated(EnumType.STRING)
    private EstadoAmistad estado;

}
