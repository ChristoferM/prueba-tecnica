package com.fondos.inversion.config;

import com.fondos.inversion.exception.RecursoNoEncontradoException;
import com.fondos.inversion.exception.ValidacionException;
import com.fondos.inversion.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiResponse<?>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        ApiResponse<?> respuesta = new ApiResponse<>(404, ex.getMessage(), null, false);
        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<ApiResponse<?>> manejarValidacion(ValidacionException ex) {
        logger.warn("Error de validación: {}", ex.getMessage());
        ApiResponse<?> respuesta = new ApiResponse<>(400, ex.getMessage(), null, false);
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> manejarExcepcionGeneral(Exception ex) {
        logger.error("Error interno del servidor: ", ex);
        ApiResponse<?> respuesta = new ApiResponse<>(500, "Error interno del servidor", null, false);
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
