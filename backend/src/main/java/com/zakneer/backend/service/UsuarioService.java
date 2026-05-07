package com.zakneer.backend.service;

import com.zakneer.backend.dto.UsuarioRequest;
import com.zakneer.backend.dto.UsuarioResponse;
import com.zakneer.backend.entity.Rol;
import com.zakneer.backend.entity.UsuarioEntity;
import com.zakneer.backend.exception.LogicaInvalidaException;
import com.zakneer.backend.repository.UsuarioRepository;
import com.zakneer.backend.utils.JwtUtils;
import com.zakneer.backend.utils.UriImagenesUtils;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UriImagenesUtils uriImagenesUtils;

    public UsuarioResponse agregarUsuario(Map<String,String> headers, UsuarioRequest usuarioRequest){
        if (usuarioRepository.findByNickname(usuarioRequest.getNickname()).isPresent()){
            throw new LogicaInvalidaException("Ya existe un usuario con nombre de usuario: " + usuarioRequest.getNickname());
        }

        verificarContraseniaSegura(usuarioRequest.getContrasenia(),usuarioRequest.getConfirmarContrasenia());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .nickname(usuarioRequest.getNickname())
                .contrasenia(encoder.encode(usuarioRequest.getContrasenia()))
                .imagenEquipo("equipo_generico.png")
                .sobresAbiertos(0)
                .rol(Rol.USUARIO)
                .build();

        usuarioRepository.save(usuarioEntity);

        return UsuarioResponse.builder()
                .nickname(usuarioEntity.getNickname())
                .build();
    }

    private void verificarContraseniaSegura(String contrasenia,String confirmarContrasenia) {

        if(contrasenia.length() < 8){
            throw new LogicaInvalidaException("La contraseña debe contener al menos 8 caracteres");
        }

        int letras = 0;
        int numeros = 0;
        int mayusculas = 0;

        for (char c : contrasenia.toCharArray()) {
            if (Character.isLetter(c)) {
                letras++;
                if (Character.isUpperCase(c)) {
                    mayusculas++;
                }
            } else if (Character.isDigit(c)) {
                numeros++;
            }
        }

        if (letras < 5) {
            throw new LogicaInvalidaException("La contraseña debe contener al menos 5 letras.");
        }
        if (numeros < 3) {
            throw new LogicaInvalidaException("La contraseña debe contener al menos 3 números.");
        }
        if (mayusculas < 1) {
            throw new LogicaInvalidaException("La contraseña debe contener al menos una letra mayúscula.");
        }

        if (!contrasenia.equals(confirmarContrasenia)){
            throw new LogicaInvalidaException("Las contraseñas no son iguales.");
        }

    }

    public UsuarioResponse obtenerUsuario(Map<String, String> headers,String nickname) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("No se encontro un usuario con en nick: " + nickname));

        return UsuarioResponse.
                builder()
                .nickname(usuarioEntity.getNickname())
                .sobresAbiertos(usuarioEntity.getSobresAbiertos())
                .imagen(uriImagenesUtils.getUrlImagen(usuarioEntity.getImagenEquipo()))
                .build();
    }
}
