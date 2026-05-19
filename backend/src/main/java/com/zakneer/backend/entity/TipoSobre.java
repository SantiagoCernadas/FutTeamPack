package com.zakneer.backend.entity;

import com.zakneer.backend.dto.sobres.*;

public enum TipoSobre {
    BASICO(60,new SobreBasico()),
    COMUN(900, new SobreComun()),
    ESPECIAL(7200,new SobreEspecial()),
    ULTIMATE(86400, new SobreUltimate());

    private final long cooldownSegundos;
    private final Sobre sobre;

    TipoSobre(long cooldownSegundos, Sobre sobre) {
        this.cooldownSegundos = cooldownSegundos;
        this.sobre = sobre;
    }

    public long getCooldownSegundos() {
        return cooldownSegundos;
    }

    public Sobre getSobre(){
        return sobre;
    }

}
