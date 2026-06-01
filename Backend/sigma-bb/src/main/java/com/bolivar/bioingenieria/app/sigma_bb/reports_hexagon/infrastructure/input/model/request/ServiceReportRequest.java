package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ServiceReportRequest", description = "DTO de entrada para crear o actualizar un reporte de servicio.")
public class ServiceReportRequest {

    @Schema(description = "Identificador de la orden de trabajo", example = "OT-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El identificador de la orden de trabajo es obligatorio")
    @Size(max = 10, message = "El identificador de la orden de trabajo no puede superar 10 caracteres")
    private String workOrderId;

    @Schema(description = "Identificador del equipo cliente", example = "EQC-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El identificador del equipo cliente es obligatorio")
    @Size(max = 10, message = "El identificador del equipo cliente no puede superar 10 caracteres")
    private String clientEquipmentId;

    @Schema(description = "Observaciones del reporte de servicio", example = "Equipo en buen estado general")
    @Size(max = 250, message = "Las observaciones no pueden superar 250 caracteres")
    private String observations;

    @Schema(description = "Resultado de la verificacion tecnica", example = "CONFORME")
    @Size(max = 50, message = "El resultado de la verificacion tecnica no puede superar 50 caracteres")
    private String technicalVerificationResult;
}
