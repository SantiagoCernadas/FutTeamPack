package com.zakneer.backend.controller;

import com.zakneer.backend.dto.EquipoRequest;
import com.zakneer.backend.dto.EquipoResponse;
import com.zakneer.backend.dto.EquipoUsuarioResponse;
import com.zakneer.backend.service.EquipoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipo")
public class EquipoController {
    @Autowired
    private EquipoService equipoService;

    @PostMapping("/agregar")
    public ResponseEntity<EquipoResponse> agregarEquipo(@RequestHeader Map<String,String> headers, @Valid @RequestBody EquipoRequest equipoRequest) {
        return ResponseEntity.ok(equipoService.agregarEquipo(headers,equipoRequest));
    }

    @GetMapping("/obtener")
    public ResponseEntity<Page<EquipoResponse>> obtenerEquipos(@RequestHeader Map<String,String> headers,@RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "5") int cantidad){
        return ResponseEntity.ok(equipoService.getEquipos(headers,pagina,cantidad));
    }

    @GetMapping("/usuario/{nickname}")
    public ResponseEntity<Page<EquipoUsuarioResponse>> obtenerEquiposUsuario(@RequestHeader Map<String,String> headers, @PathVariable String nickname,@RequestParam(defaultValue = "0") int pagina,
                                                                             @RequestParam(defaultValue = "5") int cantidad){
        return ResponseEntity.ok(equipoService.getEquiposUsuario(headers,nickname,pagina,cantidad));
    }
}
