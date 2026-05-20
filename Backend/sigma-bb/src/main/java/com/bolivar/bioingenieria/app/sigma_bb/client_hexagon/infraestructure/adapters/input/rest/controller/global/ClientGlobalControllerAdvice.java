package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller.global;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception.ClientEquipmentFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception.ClientNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception.HeadquarterFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.exception.ServiceAreaFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.error_model.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.utils.ErrorCatalog.*;
import static com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils.ErrorCatalog.DATABASE_ERROR;
import static com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils.ErrorCatalog.UNAUTHORIZED;
import static com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils.ErrorCatalog.UNKNOWN_ERROR;

/**
 * Manejador global de excepciones para el módulo de clientes.
 *
 * Esta clase centraliza el manejo de errores lanzados por los controladores,
 * permitiendo devolver respuestas consistentes y estructuradas (ErrorResponse).
 *
 * Está asociada al contexto "clientGlobalControllerAdvice", aplicando
 * sus manejadores únicamente a los controladores definidos en ese ámbito.
 *
 * Incluye el manejo de:
 * - Errores de validación de datos
 * - Recursos no encontrados
 * - Problemas de acceso a datos
 * - Errores de seguridad
 * - Excepciones genéricas
 */
@RestControllerAdvice("clientGlobalControllerAdvice")
public class ClientGlobalControllerAdvice {

    /**
     * Maneja la excepción cuando no se encuentra un cliente.
     *
     * Retorna una respuesta de error con:
     * - Código de error.
     * - Mensaje descriptivo.
     * - Fecha y hora en que ocurrió el error.
     *
     * @return ErrorResponse con la información del error.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClientNotFoundException.class)
    public ErrorResponse handleClientNotFoundException() {
        return ErrorResponse.builder()
                .code(CLIENT_NOT_FOUND.getCode())
                .message(CLIENT_NOT_FOUND.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Maneja la excepción cuando no se encuentra una sede.
     *
     * Retorna una respuesta de error con:
     * - Código de error.
     * - Mensaje descriptivo.
     * - Fecha y hora en que ocurrió el error.
     *
     * @return ErrorResponse con la información del error.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HeadquarterFoundException.class)
    public ErrorResponse handleHeadquarterNotFoundException() {
        return ErrorResponse.builder()
                .code(HEADQUARTER_NOT_FOUND.getCode())
                .message(HEADQUARTER_NOT_FOUND.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Maneja la excepción cuando no se encuentra un área de servicio.
     *
     * Retorna una respuesta de error con:
     * - Código de error.
     * - Mensaje descriptivo.
     * - Fecha y hora en que ocurrió el error.
     *
     * @return ErrorResponse con la información del error.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ServiceAreaFoundException.class)
    public ErrorResponse handleServiceAreaNotFoundException() {
        return ErrorResponse.builder()
                .code(SERVICE_AREA_NOT_FOUND.getCode())
                .message(SERVICE_AREA_NOT_FOUND.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Maneja la excepción cuando no se encuentra un equipo de cliente.
     *
     * Retorna una respuesta de error con:
     * - Código de error.
     * - Mensaje descriptivo.
     * - Fecha y hora en que ocurrió el error.
     *
     * @return ErrorResponse con la información del error.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClientEquipmentFoundException.class)
    public ErrorResponse handleClientEquipmentNotFoundException() {
        return ErrorResponse.builder()
                .code(CLIENT_EQUIPMENT_NOT_FOUND.getCode())
                .message(CLIENT_EQUIPMENT_NOT_FOUND.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Maneja la excepción de validación de datos para clientes.
     *
     * Se ejecuta cuando los datos de entrada no cumplen con las restricciones
     * definidas en los DTOs de solicitud, retornando una respuesta con estado
     * HTTP 400 (BAD_REQUEST) que incluye:
     * - Código de error específico para datos de cliente inválidos.
     * - Mensaje descriptivo del error.
     * - Lista de detalles con los mensajes de validación específicos para cada campo.
     * - Fecha y hora en que ocurrió el error.
     *
     * @param ex Excepción lanzada por la validación fallida
     * @return ErrorResponse con la información del error
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleClientMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();

        return ErrorResponse.builder()
                .code(INVALID_DATA.getCode())
                .message(INVALID_DATA.getMessage())
                .details(result.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build();

    }

    /**
     * Maneja errores relacionados con el acceso a la base de datos.
     *
     * Se ejecuta cuando ocurre una {@link DataAccessException} y retorna
     * una respuesta con:
     * - Código de error.
     * - Mensaje general de fallo en base de datos.
     * - Lista de detalles vacía.
     * - Fecha y hora del error.
     *
     * @return ErrorResponse con la información del error.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public ErrorResponse handleDataAccessException() {
        return ErrorResponse.builder()
                .code(DATABASE_ERROR.getCode())
                .message(DATABASE_ERROR.getMessage())
                .details(Collections.emptyList())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Maneja excepciones genéricas no controladas en la aplicación.
     *
     * Se ejecuta cuando ocurre cualquier excepción no manejada específicamente,
     * retornando una respuesta con estado HTTP 500 (INTERNAL_SERVER_ERROR) que incluye:
     * - Código de error
     * - Mensaje general
     * - Lista de detalles vacía
     * - Fecha y hora en que ocurrió el error
     *
     * @return ErrorResponse con la información del error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException() {
        return ErrorResponse.builder()
                .code(UNKNOWN_ERROR.getCode())
                .message(UNKNOWN_ERROR.getMessage())
                .details(Collections.emptyList())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Maneja excepciones de seguridad, como intentos de acceso no autorizado.
     *
     * Se ejecuta cuando ocurre una {@link SecurityException} y retorna
     * una respuesta con:
     * - Código de error.
     * - Mensaje general de acceso no autorizado.
     * - Lista de detalles vacía.
     * - Fecha y hora del error.
     *
     * @return ErrorResponse con la información del error.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SecurityException.class)
    public ErrorResponse handleSecurityException() {
        return ErrorResponse.builder()
                .code(UNAUTHORIZED.getCode())
                .message(UNAUTHORIZED.getMessage())
                .details(Collections.emptyList())
                .timestamp(LocalDateTime.now())
                .build();
    }


}
