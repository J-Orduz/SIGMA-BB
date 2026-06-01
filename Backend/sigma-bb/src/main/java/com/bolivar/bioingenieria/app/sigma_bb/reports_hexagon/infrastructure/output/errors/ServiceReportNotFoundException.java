package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.errors;

public class ServiceReportNotFoundException extends RuntimeException {
    public ServiceReportNotFoundException(String id) {
        super("Service report with id " + id + " not found");
    }
}
