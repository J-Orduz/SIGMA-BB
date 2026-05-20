package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.errors;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.response.ErrorResponse;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.errors.CityNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.errors.CountryNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@RestControllerAdvice
public class LocationGlobalHandlerError {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CountryNotFoundException.class)
    public ErrorResponse handleCountryNotFoundException(CountryNotFoundException ex) {
        return ErrorResponse.builder()
                .code("COUNTRY_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CityNotFoundException.class)
    public ErrorResponse handleCityNotFoundException(CityNotFoundException ex) {
        return ErrorResponse.builder()
                .code("CITY_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    /*
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();

        return ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed")
                .details(result.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception ex) {
        return ErrorResponse.builder()
                .code("UNKNOWN_ERROR")
                .message("An unexpected error occurred")
                .details(Collections.singletonList(ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
    }*/
}
