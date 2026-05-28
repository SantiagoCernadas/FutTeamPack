package com.zakneer.backend.service;

import com.zakneer.backend.dto.AmistadSolicitudRequest;
import com.zakneer.backend.dto.UsuarioResponse;
import com.zakneer.backend.entity.AmistadEntity;
import com.zakneer.backend.entity.EstadoAmistad;
import com.zakneer.backend.entity.UsuarioEntity;
import com.zakneer.backend.exception.LogicaInvalidaException;
import com.zakneer.backend.repository.AmistadRepository;
import com.zakneer.backend.repository.UsuarioRepository;
import com.zakneer.backend.utils.JwtUtils;
import com.zakneer.backend.utils.UriImagenesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AmistadService {
    @Autowired
    private AmistadRepository amistadRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UriImagenesUtils uriImagenesUtils;

    public List<UsuarioResponse> getAmigosUsuario(Map<String,String> headers){
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));

        List<AmistadEntity> relaciones = amistadRepository.findAmigosUsuario(usuarioEntity.getId());
        List<UsuarioResponse> response = new ArrayList<>();

        for (AmistadEntity amistadEntity : relaciones){
            if (amistadEntity.getUsuarioEnvia().getId().equals(usuarioEntity.getId())){
                response.add(UsuarioResponse.
                        builder().
                        nickname(amistadEntity.getUsuarioRecibe().getNickname()).
                        sobresAbiertos(amistadEntity.getUsuarioRecibe().getSobresAbiertos()).
                        imagen(uriImagenesUtils.getUrlImagen(amistadEntity.getUsuarioRecibe().getImagenEquipo())).
                        build());
            }
            else {
                response.add(UsuarioResponse.
                        builder().
                        nickname(amistadEntity.getUsuarioEnvia().getNickname()).
                        sobresAbiertos(amistadEntity.getUsuarioEnvia().getSobresAbiertos()).
                        imagen(uriImagenesUtils.getUrlImagen(amistadEntity.getUsuarioEnvia().getImagenEquipo())).
                        build());
            }
        }

        return response;
    }

    public List<UsuarioResponse> getSolicitudesAmistadPendientes(Map<String,String> headers){
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));

        List<AmistadEntity> solicitudes = amistadRepository
                .findSolicitudesRecibidasPendientesUsuario(usuarioEntity.getId());

        List<UsuarioResponse> response = new ArrayList<>();

        for (AmistadEntity amistadEntity : solicitudes){
            response.add(UsuarioResponse.
                    builder().
                    nickname(amistadEntity.getUsuarioEnvia().getNickname()).
                    sobresAbiertos(amistadEntity.getUsuarioEnvia().getSobresAbiertos()).
                    imagen(uriImagenesUtils.getUrlImagen(amistadEntity.getUsuarioEnvia().getImagenEquipo())).
                    build());
        }

        return response;
    }

    @Transactional
    public void enviarSolicitudAmistad(Map<String,String> headers,String usuarioReceptor){
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));

        if (nickname.equalsIgnoreCase(usuarioReceptor)){
            throw new LogicaInvalidaException("No puedes agregarte a ti mismo.");
        }

        UsuarioEntity usuarioEntityReceptor = usuarioRepository.findByNickname(usuarioReceptor)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + usuarioReceptor));

        if (amistadRepository.findSolicitudAceptada(usuarioEntity.getId(),usuarioEntityReceptor.getId()).isPresent()){
            throw new LogicaInvalidaException("Ya tienes agregado a este usuario.");
        }

        if (amistadRepository.findSolicitudPendienteAmistad(usuarioEntity.getId(),usuarioEntityReceptor.getId()).isPresent()){
            throw new LogicaInvalidaException("Ya existe una solicitud de amistad con este usuario en curso.");
        }

        int cantRechazos = amistadRepository
                .findSolicitudesRechazadas(usuarioEntity.getId(),usuarioEntityReceptor.getId()).size();

        if (cantRechazos >= 3){
            throw new LogicaInvalidaException("Ya no es posible enviarle solicitudes a este usuario.");
        }

        int cantSolicitudesReceptor = amistadRepository
                .findSolicitudesRecibidasPendientesUsuario(usuarioEntityReceptor.getId()).size();

        if (cantSolicitudesReceptor >= 20){
            throw new LogicaInvalidaException("El usuario tiene la bandeja llena. Pidele que rechaze o acepte solicitudes actuales.");
        }

        amistadRepository.save(AmistadEntity
                .builder().usuarioEnvia(usuarioEntity)
                        .usuarioRecibe(usuarioEntityReceptor)
                        .estado(EstadoAmistad.PENDIENTE)
                .build());
    }

    @Transactional
    public UsuarioResponse responderSolicitudAmistad(Map<String,String> headers, AmistadSolicitudRequest amistadSolicitudRequest){
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);
        UsuarioEntity usuarioEntityResponde = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));

        if (nickname.equals(amistadSolicitudRequest.getUsuarioAmistad())){
            throw new LogicaInvalidaException("No puedes agregarte a ti mismo.");
        }

        UsuarioEntity usuarioEntityEnvia = usuarioRepository.findByNickname(amistadSolicitudRequest.getUsuarioAmistad())
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + amistadSolicitudRequest.getUsuarioAmistad()));


        if (amistadRepository.findSolicitudAceptada(usuarioEntityResponde.getId(),usuarioEntityEnvia.getId()).isPresent()){
            throw new LogicaInvalidaException("Ya tienes agregado a este usuario.");
        }

        Optional<AmistadEntity> amistadEntity = amistadRepository.
                findSolicitudPendienteUsuario(usuarioEntityEnvia.getId(), usuarioEntityResponde.getId());

        if (amistadEntity.isEmpty()){
            throw new LogicaInvalidaException("No tienes solicitudes pendientes del usuario indicado.");
        }

        AmistadEntity solicitud = amistadEntity.get();
        if (amistadSolicitudRequest.isRespuesta()){
            solicitud.setEstado(EstadoAmistad.ACEPTADA);
        }
        else {
            solicitud.setEstado(EstadoAmistad.RECHAZADA);
        }

        amistadRepository.save(solicitud);

        return UsuarioResponse.
                builder().
                nickname(usuarioEntityEnvia.getNickname()).
                sobresAbiertos(usuarioEntityEnvia.getSobresAbiertos()).
                imagen(uriImagenesUtils.getUrlImagen(usuarioEntityEnvia.getImagenEquipo())).
                build();
    }

    public void eliminarUsuario(Map<String, String> headers, String usuarioEliminar) {
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));

        if (nickname.equalsIgnoreCase(usuarioEliminar)){
            throw new LogicaInvalidaException("No puedes eliminarte a ti mismo.");
        }

        UsuarioEntity usuarioEntityEliminar = usuarioRepository.findByNickname(usuarioEliminar)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + usuarioEliminar));

        Optional<AmistadEntity> amistad = amistadRepository.findSolicitudAceptada(usuarioEntity.getId(),usuarioEntityEliminar.getId());

        if (amistad.isEmpty()){
            throw new LogicaInvalidaException("No tienes agregado a este usuario.");
        }
        amistadRepository.delete(amistad.get());
    }
}
