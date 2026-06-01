package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ServiceReportPatchRequest", description = "DTO de entrada para actualizar parcialmente un reporte de servicio.")
public class ServiceReportPatchRequest {
    @Size(max = 10, message = "El identificador de la orden de trabajo no puede superar 10 caracteres")
    private String workOrderId;

    @Size(max = 10, message = "El identificador del equipo cliente no puede superar 10 caracteres")
    private String clientEquipmentId;

    @Size(max = 250, message = "Las observaciones no pueden superar 250 caracteres")
    private String observations;

    @Size(max = 50, message = "El resultado de la verificacion tecnica no puede superar 50 caracteres")
    private String technicalVerificationResult;
}
