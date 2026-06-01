package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportsErrorResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
}
