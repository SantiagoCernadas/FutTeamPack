package com.zakneer.backend.dto.sobres;

import com.zakneer.backend.dto.EquipoResponse;
import com.zakneer.backend.entity.*;
import com.zakneer.backend.exception.LogicaInvalidaException;
import com.zakneer.backend.repository.EquipoRepository;
import com.zakneer.backend.repository.SobreRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SobreComun implements Sobre{

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
        double random = Math.random() * 100;
        System.out.println(random);
        /*
         * S+: 1%
         * S: 3%
         * A: 10%
         * B: 20%
         * C: 25%
         * D: 41%
         * */
        if (random <= 1) return "S+";
        if (random <= 4) return "S";
        if (random <= 14) return "A";
        if (random <= 34) return "B";
        if (random <= 59) return "C";
        return "D";
    }

    @Override
    public Integer getCantEquipos() {
        return 5;
    }

    @Override
    public boolean puedeAbrirse(UsuarioEntity usuario, SobreRepository sobreRepository) {
        TipoSobre tipoSobre = TipoSobre.COMUN;
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
