package com.zakneer.backend.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

public class EstadoSobreId implements Serializable {

    private Long usuario;
    private TipoSobre tipoSobre;

    public EstadoSobreId() {
    }

    public EstadoSobreId(Long usuario, TipoSobre tipoSobre) {
        this.usuario = usuario;
        this.tipoSobre = tipoSobre;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadoSobreId that = (EstadoSobreId) o;
        return Objects.equals(usuario, that.usuario) &&
                Objects.equals(tipoSobre, that.tipoSobre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, tipoSobre);
    }
}