package com.zakneer.backend.service;

import com.zakneer.backend.dto.EquipoUsuarioResponse;
import com.zakneer.backend.dto.LigaResponse;
import com.zakneer.backend.dto.PaisResponse;
import com.zakneer.backend.dto.SobreResponse;
import com.zakneer.backend.dto.sobres.Sobre;
import com.zakneer.backend.entity.*;
import com.zakneer.backend.exception.LogicaInvalidaException;
import com.zakneer.backend.repository.EquipoRepository;
import com.zakneer.backend.repository.SobreRepository;
import com.zakneer.backend.repository.UsuarioRepository;
import com.zakneer.backend.repository.UsuarioXEquipoRepository;
import com.zakneer.backend.utils.JwtUtils;
import com.zakneer.backend.utils.UriImagenesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class PackService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UriImagenesUtils uriImagenesUtils;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioXEquipoRepository usuarioXEquipoRepository;

    @Autowired
    private SobreRepository sobreRepository;

    @Transactional
    public List<EquipoUsuarioResponse> abrirSobre(Map<String, String> headers, TipoSobre tipoSobre) {
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));


        if (!tipoSobre.getSobre().puedeAbrirse(usuarioEntity,sobreRepository)){
            throw new LogicaInvalidaException("No es posible abrir el sobre por el momento.");
        }

        EstadoSobreId id = new EstadoSobreId(usuarioEntity.getId(), tipoSobre);
        SobreEntity estado = sobreRepository.findById(id)
                .orElse(new SobreEntity(usuarioEntity, tipoSobre, null));

        Instant ahora = Instant.now();
        estado.setHoraApertura(ahora);
        sobreRepository.save(estado);

        List<EquipoEntity> equiposEntity = tipoSobre.getSobre().abrirSobre(equipoRepository);
        List<EquipoUsuarioResponse> equiposResponse = new ArrayList<>();


        for (EquipoEntity equipo: equiposEntity){
            UsuarioEquipoId usuarioEquipoId = new UsuarioEquipoId(usuarioEntity.getId(),equipo.getId());
            UsuarioXEquipoEntity usuarioXEquipoEntity = usuarioXEquipoRepository.findById(usuarioEquipoId).orElse(null);
            int cantidad;
            if (usuarioXEquipoEntity == null){
                cantidad = 1;
                usuarioXEquipoEntity = new UsuarioXEquipoEntity(usuarioEntity,equipo,cantidad);
                usuarioXEquipoRepository.save(usuarioXEquipoEntity);
            }
            else {
                cantidad = usuarioXEquipoEntity.getCantidad() + 1;
                usuarioXEquipoEntity.setCantidad(cantidad);
                usuarioXEquipoRepository.save(usuarioXEquipoEntity);
            }


            equiposResponse.add(
                    EquipoUsuarioResponse.builder()
                            .nombre(equipo.getNombre())
                            .pais(new PaisResponse(equipo.getLiga().getPais().getNombre(), uriImagenesUtils.getUrlImagen(equipo.getLiga().getPais().getUriImagen())))
                            .liga(new LigaResponse(equipo.getLiga().getNombre(),uriImagenesUtils.getUrlImagen(equipo.getLiga().getUriImagen())))
                            .tier(equipo.getTier().getTier())
                            .imagen(uriImagenesUtils.getUrlImagen(equipo.getUriImagen()))
                            .cantidad(cantidad)
                            .build());
        }



        return equiposResponse;
    }


    public List<SobreResponse> obtenerSobresDisponibles(Map<String, String> headers) {
        List<TipoSobre> tiposSobres = new ArrayList<>();
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));

        List<SobreResponse> listaResponse = new ArrayList<>();
        Instant ahora = Instant.now();

        for (TipoSobre tipo : TipoSobre.values()) {
            // Buscamos el estado en la tabla que creamos (id usuario + tipo sobre)
            EstadoSobreId id = new EstadoSobreId(usuarioEntity.getId(), tipo);
            Optional<SobreEntity> estadoOpt = sobreRepository.findById(id);

            long segundosRestantes = 0;
            boolean disponible = true;

            if (estadoOpt.isPresent() && tipo.getCooldownSegundos() > 0) {
                long transcurrido = Duration.between(estadoOpt.get().getHoraApertura(), ahora).getSeconds();
                segundosRestantes = tipo.getCooldownSegundos() - transcurrido;
                if (segundosRestantes < 0){
                    segundosRestantes = 0;
                }
                if (!tipo.getSobre().puedeAbrirse(usuarioEntity,sobreRepository)) {
                    disponible = false;
                }
            }

            listaResponse.add(SobreResponse.builder()
                    .tipoSobre(tipo)
                    .disponible(disponible)
                    .segundosRestantes(segundosRestantes)
                    .imagen(uriImagenesUtils.getUrlImagen("sobre_"+tipo.name().toLowerCase()+".png"))
                    .tiempoRestanteFormateado(formatearTiempo(segundosRestantes))
                    .build());
        }
        return listaResponse;
    }

    private String formatearTiempo(long totalSegundos) {
        long h = totalSegundos / 3600;
        long m = (totalSegundos % 3600) / 60;
        long s = totalSegundos % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
