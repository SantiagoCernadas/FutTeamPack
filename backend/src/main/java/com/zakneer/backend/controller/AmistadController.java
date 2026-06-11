package com.zakneer.backend.controller;

import com.zakneer.backend.dto.AmistadSolicitudRequest;
import com.zakneer.backend.dto.UsuarioResponse;
import com.zakneer.backend.service.AmistadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/amistad")
public class AmistadController {

    @Autowired
    private AmistadService amistadService;

    @GetMapping()
    public ResponseEntity<List<UsuarioResponse>> getAmigosUsuario(@RequestHeader Map<String,String> headers){
        return ResponseEntity.ok(amistadService.getAmigosUsuario(headers));
    }

    @DeleteMapping("/{nickname}")
    public ResponseEntity<Object> eliminarUsuario(@RequestHeader Map<String,String> headers,
                                                  @PathVariable String nickname){
        amistadService.eliminarUsuario(headers,nickname);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/solicitud")
    public ResponseEntity<List<UsuarioResponse>> getSolicitudesAmistadPendientes(@RequestHeader Map<String,String> headers){
        return ResponseEntity.ok(amistadService.getSolicitudesAmistadPendientes(headers));
    }

    @PostMapping("/solicitud/enviar/{nickname}")
    public ResponseEntity<Object> enviarSolicitudAmistad
            (@RequestHeader Map<String,String> headers,
             @PathVariable  String nickname){
        amistadService.enviarSolicitudAmistad(headers,nickname);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/solicitud/responder")
    public ResponseEntity<UsuarioResponse> responderSolicitudAmistad
            (@RequestHeader Map<String,String> headers,
             @RequestBody AmistadSolicitudRequest request){
        return ResponseEntity.ok(amistadService.responderSolicitudAmistad(headers,request));
    }
}
