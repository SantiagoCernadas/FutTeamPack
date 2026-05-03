package com.zakneer.backend.exception;

import com.zakneer.backend.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Hidden
public class ControllerAdvice {
    @ExceptionHandler(BadCredentialsException.class)
    @Hidden
    public ResponseEntity<ErrorResponse> credencialesIncorrectas(Exception ex, WebRequest request) {

        String path = request.getDescription(false).replace("uri=", "");

        ErrorResponse error = ErrorResponse.builder()
                .error("Credenciales invalidas.")
                .estado(401)
                .mensaje("Usuario o contraseña invalidos.")
                .ruta(path)
                .tiempo(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LogicaInvalidaException.class)
    @Hidden
    public ResponseEntity<ErrorResponse> logicaInvalida(LogicaInvalidaException ex,WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        ErrorResponse error = ErrorResponse.builder()
                .error("Logica invalida")
                .estado(400)
                .mensaje(ex.getMessage())
                .ruta(path)
                .tiempo(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @Hidden
    public ResponseEntity<ErrorResponse> logicaInvalida(AccessDeniedException ex,WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        ErrorResponse error = ErrorResponse.builder()
                .error("Acceso denegado")
                .estado(403)
                .mensaje("No cuentas con los permisos para acceder a este recurso.")
                .ruta(path)
                .tiempo(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ErrorResponse> errorAutenticacion(InsufficientAuthenticationException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        ErrorResponse error = ErrorResponse.builder()
                .error("Sesion invalida.")
                .estado(401)
                .mensaje("Sesion invalida. Vuelve a iniciar sesion.")
                .ruta(path)
                .tiempo(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @Hidden
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex,WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        ErrorResponse error = ErrorResponse.builder()
                .error("Faltan parametros.")
                .estado(400)
                .mensaje("Ingresar el campo: " + ex.getParameterName())
                .ruta(path)
                .tiempo(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Hidden
    public ResponseEntity<ErrorResponse> handleMissingParams(MethodArgumentNotValidException ex,WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        ErrorResponse error = ErrorResponse.builder()
                .error("Faltan parametros.")
                .estado(400)
                .mensaje("Argumentos no validos")
                .ruta(path)
                .tiempo(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @Hidden
    public ResponseEntity<ErrorResponse> errorGenerico(Exception ex,WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");

        ErrorResponse error = ErrorResponse.builder()
                .error("ERROR INTERNO.")
                .estado(500)
                .mensaje(ex.getMessage())
                .ruta(path)
                .tiempo(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
