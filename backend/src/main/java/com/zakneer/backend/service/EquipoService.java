package com.zakneer.backend.service;

import com.zakneer.backend.dto.*;
import com.zakneer.backend.entity.*;
import com.zakneer.backend.exception.LogicaInvalidaException;
import com.zakneer.backend.repository.*;
import com.zakneer.backend.utils.JwtUtils;
import com.zakneer.backend.utils.UriImagenesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class EquipoService {

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
    private LigaRepository ligaRepository;

    @Autowired
    private TierRepository tierRepository;


    @PreAuthorize("hasRole('ADMIN')")
    public EquipoResponse agregarEquipo(Map<String, String> headers, EquipoRequest equipoRequest){
        TierEntity tierEntity = tierRepository.findByTier(equipoRequest.getTier())
                .orElseThrow(() -> new NoSuchElementException("Tier: " + equipoRequest.getTier() + "inexistente"));

        LigaEntity ligaEntity = ligaRepository.findByNombre(equipoRequest.getLiga())
                .orElseThrow(() -> new NoSuchElementException("Liga: " + equipoRequest.getLiga() + "inexistente"));


        if (equipoRepository.existsByNombreAndLigaId(equipoRequest.getNombre(),ligaEntity.getId())){
            throw new LogicaInvalidaException("Ya existe el equipo: " + equipoRequest.getNombre() + " en la liga: " + equipoRequest.getLiga());
        }

        EquipoEntity equipoEntity = EquipoEntity
                .builder()
                .nombre(equipoRequest.getNombre())
                .tier(tierEntity)
                .liga(ligaEntity)
                .uriImagen(equipoRequest.getImagen())
                .build();

        equipoRepository.save(equipoEntity);

        return EquipoResponse.
                builder()
                .nombre(equipoEntity.getNombre())
                .tier(equipoEntity.getTier().getTier())
                .liga(equipoEntity.getLiga().getNombre())
                .pais(equipoEntity.getLiga().getPais().getNombre())
                .imagen(uriImagenesUtils.getUrlImagen(equipoEntity.getUriImagen()))
                .build();
    }


    public List<EquipoUsuarioResponse> getEquiposUsuario(Map<String, String> headers){
        String token = headers.get("Authorization").substring(7);
        String nickname = jwtUtils.getNicknameFromToken(token);

        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname).orElseThrow(
                () -> new LogicaInvalidaException("Usuario no encontrado.")
        );

        List<UsuarioXEquipoEntity> listaEntity = usuarioXEquipoRepository.getUsuarioXEquipoById_usuario(usuarioEntity.getId());
        List<EquipoUsuarioResponse> listaResponse = new ArrayList<>();

        for (UsuarioXEquipoEntity entity: listaEntity){
            EquipoUsuarioResponse response = EquipoUsuarioResponse
                    .builder()
                    .nombre(entity.getEquipo().getNombre())
                    .pais(new PaisResponse(entity.getEquipo().getLiga().getPais().getNombre(),uriImagenesUtils.getUrlImagen(entity.getEquipo().getLiga().getPais().getUriImagen())))
                    .liga(new LigaResponse(entity.getEquipo().getLiga().getNombre(), uriImagenesUtils.getUrlImagen(entity.getEquipo().getLiga().getUriImagen())))
                    .tier(entity.getEquipo().getTier().getTier())
                    .imagen(uriImagenesUtils.getUrlImagen(entity.getEquipo().getUriImagen()))
                    .cantidad(entity.getCantidad())
                    .build();

            listaResponse.add(response);
        }

        return listaResponse;
    }

}
