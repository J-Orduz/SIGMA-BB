package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.domain.model;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ServiceReport {
    private UUID id;
    private String workOrderId;
    private String clientEquipmentId;
    private String observations;
    private String technicalVerificationResult;

    public static ServiceReport create(String workOrderId,
                                       String clientEquipmentId,
                                       String observations,
                                       String technicalVerificationResult) {
        return ServiceReport.builder()
                .id(UUID.randomUUID())
                .workOrderId(workOrderId)
                .clientEquipmentId(clientEquipmentId)
                .observations(observations)
                .technicalVerificationResult(technicalVerificationResult)
                .build();
    }

    public void update(String workOrderId,
                       String clientEquipmentId,
                       String observations,
                       String technicalVerificationResult) {
        this.workOrderId = workOrderId;
        this.clientEquipmentId = clientEquipmentId;
        this.observations = observations;
        this.technicalVerificationResult = technicalVerificationResult;
    }

    public void patch(String workOrderId,
                      String clientEquipmentId,
                      String observations,
                      String technicalVerificationResult) {
        if (workOrderId != null) {
            this.workOrderId = workOrderId;
        }
        if (clientEquipmentId != null) {
            this.clientEquipmentId = clientEquipmentId;
        }
        if (observations != null) {
            this.observations = observations;
        }
        if (technicalVerificationResult != null) {
            this.technicalVerificationResult = technicalVerificationResult;
        }
    }
}
