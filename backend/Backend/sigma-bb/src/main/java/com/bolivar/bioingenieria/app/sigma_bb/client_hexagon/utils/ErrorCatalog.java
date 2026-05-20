package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.utils;

import lombok.Getter;

/**
 * Enumerado que define un catálogo de errores para la aplicación.
 *
 * <p>Cada constante representa un error específico que puede ocurrir en el sistema,
 * con un código único y un mensaje descriptivo asociado.</p>
 */
@Getter
public enum ErrorCatalog {

    /**
     * Error cuando un Cliente no es encontrado en la base de datos.
     * Código: ERR_CLIENT_001
     */
    CLIENT_NOT_FOUND("ERR_CLIENT_001", "Client not found"),

    /**
     * Error cuando una Sede no es encontrada en la base de datos.
     * Código: ERR_HEADQUARTER_001
     */
    HEADQUARTER_NOT_FOUND("ERR_HEADQUARTER_001", "Headquarter not found"),

    /**
     * Error cuando un Área de Servicio no es encontrada en la base de datos.
     * Código: ERR_SERVICE_AREA_001
     */
    SERVICE_AREA_NOT_FOUND("ERR_SERVICE_AREA_001", "Service area not found"),

    /**
     * Error cuando un Equipo de Cliente no es encontrado en la base de datos.
     * Código: ERR_CLIENT_EQUIPMENT_001
     */
    CLIENT_EQUIPMENT_NOT_FOUND("ERR_CLIENT_EQUIPMENT_001", "Client equipment not found"),

    /**
     * Error cuando los datos proporcionados para un Cliente son inválidos
     * o no cumplen con las restricciones de validación.
     * Código: ERR_CLIENT_002
     */
    INVALID_DATA("ERR_INVALID_DATA", "Invalid data"),

    /**
     * Error general de base de datos, como problemas de conexión o consultas fallidas.
     * Código: ERR_DATABASE_001
     */
    DATABASE_ERROR("ERR_DATABASE_001", "Error al acceder a la base de datos"),

    /**
    * Error general desconocido que no encaja en ninguna categoría específica.
    * Código: ERR_UNKNOWN_999
    */
    UNKNOWN_ERROR("ERR_UNKNOWN_999", "Unknown error"),

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
