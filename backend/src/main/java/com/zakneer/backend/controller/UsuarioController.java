package com.zakneer.backend.controller;

import com.zakneer.backend.dto.UsuarioRequest;
import com.zakneer.backend.dto.UsuarioResponse;
import com.zakneer.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<UsuarioResponse> agregarUsuario(@RequestHeader Map<String,String> headers,
                                                          @Valid @RequestBody UsuarioRequest usuarioRequest){
        return ResponseEntity.ok(usuarioService.agregarUsuario(headers,usuarioRequest));
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@RequestHeader Map<String,String> headers,@PathVariable String nickname){
        return ResponseEntity.ok(usuarioService.obtenerUsuario(headers,nickname));
    }

}
