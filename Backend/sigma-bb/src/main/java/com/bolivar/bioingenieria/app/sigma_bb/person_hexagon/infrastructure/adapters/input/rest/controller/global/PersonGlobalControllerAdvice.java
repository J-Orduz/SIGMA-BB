package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.controller.global;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.exception.PersonNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.error_model.ErrorResponse;
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

import static com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils.ErrorCatalog.*;

/**
 * Manejador global de excepciones para el módulo de personas.
 *
 * Esta clase centraliza el manejo de errores lanzados por los controladores,
 * permitiendo devolver respuestas consistentes y estructuradas (ErrorResponse).
 *
 * Está asociada al contexto "personGlobalControllerAdvice", aplicando
 * sus manejadores únicamente a los controladores definidos en ese ámbito.
 *
 * Incluye el manejo de:
 * - Errores de validación de datos
 * - Recursos no encontrados
 * - Problemas de acceso a datos
 * - Errores de seguridad
 * - Excepciones genéricas
 */
@RestControllerAdvice("personGlobalControllerAdvice")
public class PersonGlobalControllerAdvice {

    /**
     * Maneja la excepción cuando no se encuentra una persona.
     *
     * Retorna una respuesta de error con:
     * - Código de error.
     * - Mensaje descriptivo.
     * - Fecha y hora en que ocurrió el error.
     *
     * @return ErrorResponse con la información del error.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PersonNotFoundException.class)
    public ErrorResponse handlePersonNotFoundException() {
        return ErrorResponse.builder()
                .code(PERSON_NOT_FOUND.getCode())
                .message(PERSON_NOT_FOUND.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Maneja las excepciones generadas cuando la validación de los datos de entrada falla.
     *
     * Este metodo captura la excepción {@link MethodArgumentNotValidException}, la cual ocurre
     * cuando un objeto anotado con @Valid no cumple con las restricciones definidas.
     *
     * Se encarga de:
     * - Obtener los errores de validación desde el BindingResult.
     * - Extraer los mensajes de error asociados a cada campo inválido.
     * - Construir una respuesta estructurada (ErrorResponse) con:
     *   - Código de error predefinido.
     *   - Mensaje general de error.
     *   - Lista de detalles con los mensajes específicos de cada campo.
     *   - Marca de tiempo del error.
     *
     * @param ex Excepción que contiene los errores de validación.
     * @return ErrorResponse con la información detallada de los errores.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();

        return ErrorResponse.builder()
                .code(INVALID_PERSON_DATA.getCode())
                .message(INVALID_PERSON_DATA.getMessage())
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
     * Maneja excepciones relacionadas con problemas de seguridad o autenticación.
     *
     * Se ejecuta cuando ocurre una {@link SecurityException}, retornando una respuesta
     * con estado HTTP 401 (UNAUTHORIZED) que incluye:
     * - Código de error
     * - Mensaje descriptivo
     * - Lista de detalles vacía
     * - Fecha y hora en que ocurrió el error
     *
     * @return ErrorResponse con la información del error
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
