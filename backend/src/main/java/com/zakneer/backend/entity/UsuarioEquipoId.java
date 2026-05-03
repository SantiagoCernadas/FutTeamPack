package com.zakneer.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioEquipoId implements Serializable {
    private Long usuario;
    private Long equipo;

    public UsuarioEquipoId() {}

    public UsuarioEquipoId(Long usuario, Long equipo) {
        this.usuario = usuario;
        this.equipo = equipo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEquipoId that = (UsuarioEquipoId) o;
        return Objects.equals(usuario, that.usuario) && Objects.equals(equipo, that.equipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, equipo);
    }
}