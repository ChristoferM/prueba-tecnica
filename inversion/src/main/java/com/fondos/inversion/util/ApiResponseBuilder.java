package com.fondos.inversion.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Clase utilitaria para crear respuestas API de forma consistente
 */
public class ApiResponseBuilder {

    /**
     * Respuesta OK (200)
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(String mensaje, T datos) {
        return ResponseEntity.ok()
                .body(new ApiResponse<>(HttpStatus.OK.value(), mensaje, datos, true));
    }

    /**
     * Respuesta CREATED (201)
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(String mensaje, T datos) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), mensaje, datos, true));
    }

    /**
     * Respuesta BAD_REQUEST (400)
     */
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String mensaje) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), mensaje, null, false));
    }

    /**
     * Respuesta NOT_FOUND (404)
     */
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String mensaje) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), mensaje, null, false));
    }

    /**
     * Respuesta INTERNAL_SERVER_ERROR (500)
     */
    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String mensaje) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), mensaje, null, false));
    }
}
