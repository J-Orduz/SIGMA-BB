package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReportResponse {
    private UUID id;
    private String workOrderId;
    private String clientEquipmentId;
    private String observations;
    private String technicalVerificationResult;
}
