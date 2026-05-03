package com.zakneer.backend.entity;

import com.zakneer.backend.dto.sobres.Sobre;
import com.zakneer.backend.dto.sobres.SobreBasico;
import com.zakneer.backend.dto.sobres.SobreComun;
import com.zakneer.backend.dto.sobres.SobreEspecial;

public enum TipoSobre {
    COMUN(3600, new SobreComun()),
    BASICO(300,new SobreBasico()),
    ESPECIAL(86400,new SobreEspecial());

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
