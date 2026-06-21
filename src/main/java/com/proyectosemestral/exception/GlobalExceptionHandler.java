package com.proyectosemestral.exception;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Captura errores de validación en los datos de entrada (HTTP 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.BAD_GATEWAY.value()); // O puedes usar HttpStatus.BAD_REQUEST.value() (400)
        response.put("error", "Error de Validación en los Datos");

        // Extrae todos los mensajes de error personalizados que definiste en el DTO
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        response.put("errores", errores);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Captura el NotFoundException (HTTP 404) cuando no exista un ID de venta
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CaidoException.class)
    public ResponseEntity<Map<String, Object>> manejarServicioCaido(CaidoException ex) {
        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        respuesta.put("error", "Servicio Externo No Disponible");
        respuesta.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(respuesta, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
