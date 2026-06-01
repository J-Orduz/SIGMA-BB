package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands;

public record UpdateServiceReportCommand(String workOrderId,
                                         String clientEquipmentId,
                                         String observations,
                                         String technicalVerificationResult) {
}
