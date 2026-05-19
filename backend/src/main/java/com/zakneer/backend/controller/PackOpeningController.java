package com.zakneer.backend.controller;

import com.zakneer.backend.dto.EquipoUsuarioResponse;
import com.zakneer.backend.dto.SobreResponse;
import com.zakneer.backend.dto.sobres.SobreComun;
import com.zakneer.backend.entity.TipoSobre;
import com.zakneer.backend.service.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/packs")
public class PackOpeningController {

    @Autowired
    private PackService packService;

    @PostMapping("/basico")
    public ResponseEntity<List<EquipoUsuarioResponse>> abrirSobreBasico(@RequestHeader Map<String,String> headers){
        return ResponseEntity.ok(packService.abrirSobre(headers, TipoSobre.BASICO));
    }

    @PostMapping("/comun")
    public ResponseEntity<List<EquipoUsuarioResponse>> abrirSobreComun(@RequestHeader Map<String,String> headers){
        return ResponseEntity.ok(packService.abrirSobre(headers, TipoSobre.COMUN));
    }

    @PostMapping("/especial")
    public ResponseEntity<List<EquipoUsuarioResponse>> abrirSobreEspecial(@RequestHeader Map<String,String> headers){
        return ResponseEntity.ok(packService.abrirSobre(headers, TipoSobre.ESPECIAL));
    }

    @PostMapping("/ultimate")
    public ResponseEntity<List<EquipoUsuarioResponse>> abrirSobreUltimate(@RequestHeader Map<String,String> headers){
        return ResponseEntity.ok(packService.abrirSobre(headers, TipoSobre.ULTIMATE));
    }


    @GetMapping()
    public ResponseEntity<List<SobreResponse>> sobresDisponibles(@RequestHeader Map<String,String> headers){
        return ResponseEntity.ok(packService.obtenerSobresDisponibles(headers));
    }
}
