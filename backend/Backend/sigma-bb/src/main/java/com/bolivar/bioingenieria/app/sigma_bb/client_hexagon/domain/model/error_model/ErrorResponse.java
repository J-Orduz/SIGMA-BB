package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.error_model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo de respuesta para errores en el sistema.
 * Contiene información estructurada del error incluyendo código, mensaje, detalles y timestamp.
 */
@Builder
@Getter
@Setter
public class ErrorResponse {

    /**
     * Código único del error para identificarlo en el catálogo de errores.
     */
    private String code;

    /**
     * Mensaje descriptivo del error.
     */
    private String message;

    /**
     * Lista de detalles adicionales o mensajes específicos sobre el error.
     */
    private List<String> details;

    /**
     * Fecha y hora en que ocurrió el error.
     */
    private LocalDateTime timestamp;
}
