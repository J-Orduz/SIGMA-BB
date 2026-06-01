package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.rest;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.ports.input.ServiceReportServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.CreateServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.DeleteServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.PatchServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.UpdateServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.domain.model.ServiceReport;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.mapper.ServiceReportRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.request.ServiceReportPatchRequest;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.request.ServiceReportRequest;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.response.ServiceReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/service-reports")
@Tag(name = "Service Report REST API", description = "Endpoints para la gestion de reportes de servicio")
public class ServiceReportController {
    private final ServiceReportServicePort serviceReportServicePort;
    private final ServiceReportRestMapper serviceReportRestMapper;

    @Autowired
    public ServiceReportController(ServiceReportServicePort serviceReportServicePort,
                                   ServiceReportRestMapper serviceReportRestMapper) {
        this.serviceReportServicePort = serviceReportServicePort;
        this.serviceReportRestMapper = serviceReportRestMapper;
    }

    @Operation(summary = "Obtener todos los reportes de servicio")
    @GetMapping
    public ResponseEntity<List<ServiceReportResponse>> getAllServiceReports() {
        return ResponseEntity.ok(serviceReportRestMapper
                .toServiceReportResponseList(serviceReportServicePort.findAll()));
    }

    @Operation(summary = "Obtener reporte de servicio por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceReportResponse> getServiceReport(
            @Parameter(description = "Identificador unico del reporte de servicio", required = true)
            @PathVariable String id) {
        return ResponseEntity.ok(serviceReportRestMapper
                .toServiceReportResponse(serviceReportServicePort.findById(id)));
    }

    @Operation(summary = "Crear nuevo reporte de servicio")
    @PostMapping
    public ResponseEntity<ServiceReportResponse> createServiceReport(
            @Valid @RequestBody ServiceReportRequest request) {
        CreateServiceReportCommand command = new CreateServiceReportCommand(
                request.getWorkOrderId(),
                request.getClientEquipmentId(),
                request.getObservations(),
                request.getTechnicalVerificationResult());

        ServiceReport created = serviceReportServicePort.save(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceReportRestMapper.toServiceReportResponse(created));
    }

    @Operation(summary = "Actualizar reporte de servicio")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceReportResponse> updateServiceReport(
            @Parameter(description = "Identificador unico del reporte de servicio", required = true)
            @PathVariable String id,
            @Valid @RequestBody ServiceReportRequest request) {
        UpdateServiceReportCommand command = new UpdateServiceReportCommand(
                request.getWorkOrderId(),
                request.getClientEquipmentId(),
                request.getObservations(),
                request.getTechnicalVerificationResult());

        ServiceReport updated = serviceReportServicePort.update(id, command);
        return ResponseEntity.ok(serviceReportRestMapper.toServiceReportResponse(updated));
    }

    @Operation(summary = "Actualizar parcialmente reporte de servicio")
    @PatchMapping("/{id}")
    public ResponseEntity<ServiceReportResponse> patchServiceReport(
            @Parameter(description = "Identificador unico del reporte de servicio", required = true)
            @PathVariable String id,
            @Valid @RequestBody ServiceReportPatchRequest request) {
        PatchServiceReportCommand command = new PatchServiceReportCommand(
                request.getWorkOrderId(),
                request.getClientEquipmentId(),
                request.getObservations(),
                request.getTechnicalVerificationResult());

        ServiceReport updated = serviceReportServicePort.patchUpdate(id, command);
        return ResponseEntity.ok(serviceReportRestMapper.toServiceReportResponse(updated));
    }

    @Operation(summary = "Eliminar reporte de servicio")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceReport(
            @Parameter(description = "Identificador unico del reporte de servicio", required = true)
            @PathVariable String id) {
        serviceReportServicePort.delete(new DeleteServiceReportCommand(id));
        return ResponseEntity.noContent().build();
    }
}
