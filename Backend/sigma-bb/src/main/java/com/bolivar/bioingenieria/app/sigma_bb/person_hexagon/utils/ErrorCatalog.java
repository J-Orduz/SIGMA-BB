package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils;

import lombok.Getter;

/**
 * Catálogo centralizado de códigos y mensajes de error para el hexágono de personas.
 *
 * Define un conjunto de errores predefinidos que pueden ocurrir en las operaciones
 * relacionadas con la gestión de personas. Cada error contiene un código único y un
 * mensaje descriptivo para facilitar el diagnóstico y la depuración.
 */
@Getter
public enum ErrorCatalog {
    /**
     * Error cuando una persona no es encontrada en la base de datos.
     * Código: ERR_PERSON_001
     */
    PERSON_NOT_FOUND("ERR_PERSON_001", "Person not found"),

    /**
     * Error cuando los datos proporcionados para una persona son inválidos
     * o no cumplen con las restricciones de validación.
     * Código: ERR_PERSON_002
     */
    INVALID_PERSON_DATA("ERR_PERSON_002", "Invalid person data"),

    /**
     * Error genérico de base de datos que puede ocurrir durante operaciones
     * de persistencia.
     * Código: ERR_DATABASE_003
     */
    DATABASE_ERROR("ERR_DATABASE_003", "Database error"),

    /**
     * Error desconocido que no corresponde a ninguno de los casos específicos
     * definidos en el catálogo.
     * Código: ERR_UNKNOW_999
     */
    UNKNOWN_ERROR("ERR_UNKNOW_999", "Unknown error"),

    /**
     * Error de autenticación y autorización cuando el usuario no tiene permisos
     * para acceder a un recurso o realizar una operación.
     * Código: ERR_AUTH_401
     */
    UNAUTHORIZED("ERR_AUTH_401", "Unauthorized access");

    private final String code;
    private final String message;

    /**
     * Constructor del enumerado ErrorCatalog.
     *
     * @param code Código único del error para identificación
     * @param message Mensaje descriptivo del error en inglés
     */
    ErrorCatalog(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
