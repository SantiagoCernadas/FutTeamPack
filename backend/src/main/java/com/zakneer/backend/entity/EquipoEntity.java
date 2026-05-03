package com.zakneer.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
@Entity(name = "equipo")
public class EquipoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tier")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TierEntity tier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_liga")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LigaEntity liga;

    @Size(max = 30)
    private String nombre;

    @Column(name = "uri_imagen")
    private String uriImagen;

}
