package com.zakneer.backend.dto.sobres;

import com.zakneer.backend.entity.*;
import com.zakneer.backend.repository.EquipoRepository;
import com.zakneer.backend.repository.SobreRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SobreUltimate implements Sobre{
    @Override
    public List<EquipoEntity> abrirSobre(EquipoRepository equipoRepository) {
        Set<EquipoEntity> equipos = new HashSet<>();
        while (equipos.size() < getCantEquipos()){
            String tier = obtenerTier();
            EquipoEntity equipo = equipoRepository.findRandomEquipoByTierName(tier);
            if(equipo != null){
                equipos.add(equipo);
            }
        }
        return new ArrayList<>(equipos);
    }

    @Override
    public String obtenerTier() {
        return "S+";
    }

    @Override
    public Integer getCantEquipos() {
        return 1;
    }

    @Override
    public boolean puedeAbrirse(UsuarioEntity usuario, SobreRepository sobreRepository) {
        TipoSobre tipoSobre = TipoSobre.ULTIMATE;
        EstadoSobreId id = new EstadoSobreId(usuario.getId(), tipoSobre);


        SobreEntity estado = sobreRepository.findById(id)
                .orElse(new SobreEntity(usuario, tipoSobre, null));

        Instant ahora = Instant.now();

        if (estado.getHoraApertura() != null){
            long segundosTranscurridos = Duration.between(estado.getHoraApertura(), ahora).getSeconds();
            if (segundosTranscurridos < tipoSobre.getCooldownSegundos()) {
                return false;
            }
        }

        return true;
    }
}
