package com.zakneer.backend.dto.sobres;

import com.zakneer.backend.dto.EquipoResponse;
import com.zakneer.backend.entity.EquipoEntity;
import com.zakneer.backend.entity.UsuarioEntity;
import com.zakneer.backend.repository.EquipoRepository;
import com.zakneer.backend.repository.SobreRepository;

import java.util.List;

public interface Sobre {
    public List<EquipoEntity> abrirSobre(EquipoRepository equipoRepository);
    public String obtenerTier();
    public Integer getCantEquipos();
    public boolean puedeAbrirse(UsuarioEntity usuario, SobreRepository sobreRepository);
}
