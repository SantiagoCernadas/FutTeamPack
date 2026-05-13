package com.zakneer.backend.service;

import com.zakneer.backend.dto.*;
import com.zakneer.backend.entity.*;
import com.zakneer.backend.exception.LogicaInvalidaException;
import com.zakneer.backend.repository.*;
import com.zakneer.backend.utils.JwtUtils;
import com.zakneer.backend.utils.UriImagenesUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

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
                .pais(new PaisResponse(
                        equipoEntity.getLiga().getPais().getNombre(),
                        uriImagenesUtils.getUrlImagen(equipoEntity.getLiga().getPais().getUriImagen())
                ))
                .liga(new LigaResponse(
                        equipoEntity.getLiga().getNombre(),
                        uriImagenesUtils.getUrlImagen(equipoEntity.getLiga().getUriImagen())
                ))
                .imagen(uriImagenesUtils.getUrlImagen(equipoEntity.getUriImagen()))
                .build();
    }


    public Page<EquipoUsuarioResponse> getEquiposUsuario(Map<String, String> headers, String nickname,int pagina,int cantidad){
        Pageable pageable = PageRequest.of(pagina,cantidad);
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname).orElseThrow(
                () -> new NoSuchElementException("Usuario: " + nickname + " Inexistente")
        );

        Page<UsuarioXEquipoEntity> listaEntity = usuarioXEquipoRepository.getUsuarioXEquipoById_usuario(usuarioEntity.getId(),pageable);

        return listaEntity.map(entity -> EquipoUsuarioResponse.builder()
                .nombre(entity.getEquipo().getNombre())
                .pais(new PaisResponse(
                        entity.getEquipo().getLiga().getPais().getNombre(),
                        uriImagenesUtils.getUrlImagen(entity.getEquipo().getLiga().getPais().getUriImagen())
                ))
                .liga(new LigaResponse(
                        entity.getEquipo().getLiga().getNombre(),
                        uriImagenesUtils.getUrlImagen(entity.getEquipo().getUriImagen())
                ))
                .tier(entity.getEquipo().getTier().getTier())
                .imagen(uriImagenesUtils.getUrlImagen(entity.getEquipo().getUriImagen()))
                .cantidad(entity.getCantidad())
                .build()
        );
    }

    public Page<EquipoResponse> getEquipos(Map<String, String> headers, int pagina, int cantidad) {
        Pageable pageable = PageRequest.of(pagina,cantidad);

        Page<EquipoEntity> listaEntity = equipoRepository.getEquipos(pageable);

        return listaEntity.map(entity -> EquipoResponse.builder()
                .nombre(entity.getNombre())
                .pais(new PaisResponse(
                        entity.getLiga().getPais().getNombre(),
                        uriImagenesUtils.getUrlImagen(entity.getLiga().getPais().getUriImagen())
                ))
                .liga(new LigaResponse(
                        entity.getLiga().getNombre(),
                        uriImagenesUtils.getUrlImagen(entity.getUriImagen())
                ))
                .tier(entity.getTier().getTier())
                .imagen(uriImagenesUtils.getUrlImagen(entity.getUriImagen()))
                .build()
        );
    }
}
