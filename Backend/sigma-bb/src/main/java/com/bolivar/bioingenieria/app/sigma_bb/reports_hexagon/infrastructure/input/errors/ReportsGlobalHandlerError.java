package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.errors;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.response.ReportsErrorResponse;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.errors.ServiceReportNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ReportsGlobalHandlerError {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ServiceReportNotFoundException.class)
    public ReportsErrorResponse handleServiceReportNotFoundException(ServiceReportNotFoundException ex) {
        return ReportsErrorResponse.builder()
                .code("SERVICE_REPORT_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
