package com.zakneer.backend.service;

import com.zakneer.backend.dto.UsuarioResponse;
import com.zakneer.backend.entity.AmistadEntity;
import com.zakneer.backend.entity.UsuarioEntity;
import com.zakneer.backend.exception.LogicaInvalidaException;
import com.zakneer.backend.repository.AmistadRepository;
import com.zakneer.backend.repository.UsuarioRepository;
import com.zakneer.backend.utils.JwtUtils;
import com.zakneer.backend.utils.UriImagenesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //Que necesito analizar:
    /*
     * 1: listar de amigos de un usuario.
     *
     * 2:cargar solicitud de amistad (save)
     * 2.1: verificar que el usuario no se este automandando una solicitud de amistad (XD)
     * 2.2: verificar si ya es amigo del usuario al cual le envio solicitud (Si es asi, no se cargara la solicitud)
     * 2.3: Verificar que el usuario no haya enviado ya una solicitud a el usuario y este en "Pendiente"
     * 2.4: en caso de que el usuario lo haya rechazado ya 3 veces, no puede volver a enviarle solicitud de amistad.
     * 2.5: si esta todo bien, se cargara la solicitud y se enviara la misma al usuario receptor.
     *
     * 3: listar solicitudes de amistad.
     * 3.1: si el usuario acepta la solicitud, podra ver el mismo en su lista de amigos tanto el como el emisor.
     * 3.2: si la rechaza, visualmente no pasara nada. Pero desaparecera la solicitud y en la base quedara como "Rechazada".
     * */

    public List<UsuarioResponse> listarAmigos(Map<String,String> headers){
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

    public void enviarSolicitudAmistad(Map<String,String> headers,String usuarioReceptor){
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));

        if (nickname.equals(usuarioReceptor)){
            throw new LogicaInvalidaException("No puedes agregarte a ti mismo.");
        }

        UsuarioEntity usuarioEntityReceptor = usuarioRepository.findByNickname(usuarioReceptor)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + usuarioReceptor));

        if (amistadRepository.findSolicitudPendiente(usuarioEntity.getId(),usuarioEntityReceptor.getId()).isPresent()){
            throw new LogicaInvalidaException("Ya existe una solicitud de amistad en curso.");
        }
        
        int cantRechazos = amistadRepository
                .findSolicitudesRechazadas(usuarioEntity.getId(),usuarioEntityReceptor.getId()).size();

        if (cantRechazos >= 3){
            throw new LogicaInvalidaException("Ya no es posible enviarle solicitudes a este usuario.");
        }
    }
}
