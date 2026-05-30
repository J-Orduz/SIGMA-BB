package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.errors;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.ErrorResponse;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.BrandNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.ManufacturerNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentTypeNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.ModelNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.MetrologicalDataNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.TechnicalVerificationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class EquipmentGlobalHandlerError {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ManufacturerNotFoundException.class)
    public ErrorResponse handleManufacturerNotFoundException(ManufacturerNotFoundException ex) {
        return ErrorResponse.builder()
                .code("MANUFACTURER_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BrandNotFoundException.class)
    public ErrorResponse handleBrandNotFoundException(BrandNotFoundException ex) {
        return ErrorResponse.builder()
                .code("BRAND_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ModelNotFoundException.class)
    public ErrorResponse handleModelNotFoundException(ModelNotFoundException ex) {
        return ErrorResponse.builder()
                .code("MODEL_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EquipmentNotFoundException.class)
    public ErrorResponse handleEquipmentNotFoundException(EquipmentNotFoundException ex) {
        return ErrorResponse.builder()
                .code("EQUIPMENT_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EquipmentTypeNotFoundException.class)
    public ErrorResponse handleEquipmentTypeNotFoundException(EquipmentTypeNotFoundException ex) {
        return ErrorResponse.builder()
                .code("EQUIPMENT_TYPE_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TechnicalVerificationNotFoundException.class)
    public ErrorResponse handleTechnicalVerificationNotFoundException(TechnicalVerificationNotFoundException ex) {
        return ErrorResponse.builder()
                .code("TECHNICAL_VERIFICATION_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MetrologicalDataNotFoundException.class)
    public ErrorResponse handleMetrologicalDataNotFoundException(MetrologicalDataNotFoundException ex) {
        return ErrorResponse.builder()
                .code("METROLOGICAL_DATA_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    /*
     * @ResponseStatus(HttpStatus.BAD_REQUEST)
     * 
     * @ExceptionHandler(MethodArgumentNotValidException.class)
     * public ErrorResponse
     * handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
     * 
     * BindingResult result = ex.getBindingResult();
     * 
     * return ErrorResponse.builder()
     * .code("VALIDATION_ERROR")
     * .message("Validation failed")
     * .details(result.getFieldErrors()
     * .stream()
     * .map(DefaultMessageSourceResolvable::getDefaultMessage)
     * .collect(Collectors.toList()))
     * .timestamp(LocalDateTime.now())
     * .build();
     * }
     */
}