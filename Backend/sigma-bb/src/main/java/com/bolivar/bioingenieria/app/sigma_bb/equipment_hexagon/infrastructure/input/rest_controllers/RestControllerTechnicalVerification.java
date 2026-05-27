package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.TechnicalVerificationServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.TechnicalVerificationPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.CreateTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.DeleteTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.technical_verification_services.commands.UpdateTechnicalVerificationCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.TechnicalVerification;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper.TechnicalVerificationRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.TechnicalVerificationPatchRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.TechnicalVerificationRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.TechnicalVerificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/technical-verifications")
@Tag(name = "TechnicalVerification REST API", description = "Endpoints para la gestión de TechnicalVerification")
public class RestControllerTechnicalVerification {
    private final TechnicalVerificationServicePort servicePort;
    private final TechnicalVerificationRestMapper restMapper;

    @Autowired
    public RestControllerTechnicalVerification(TechnicalVerificationServicePort servicePort,
                                               TechnicalVerificationRestMapper restMapper) {
        this.servicePort = servicePort;
        this.restMapper = restMapper;
    }

    @Operation(
            summary = "Obtener todas las verificaciones técnicas",
            description = "Recupera la lista completa de verificaciones técnicas registradas en el sistema.")
    @GetMapping
    public ResponseEntity<List<TechnicalVerificationResponse>> getAll() {
        return ResponseEntity.ok(restMapper.toTechnicalVerificationResponseList(servicePort.findAll()));
    }

    @Operation(
            summary = "Obtener verificación técnica por ID",
            description = "Recupera la información de una verificación técnica específica utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<TechnicalVerificationResponse> getById(
            @Parameter(description = "Identificador único de la verificación técnica", required = true)
            @PathVariable String id) {
        return ResponseEntity.ok(restMapper.toTechnicalVerificationResponse(servicePort.findById(id)));
    }

    @Operation(
            summary = "Crear nueva verificación técnica",
            description = "Crea una nueva verificación técnica en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<TechnicalVerificationResponse> create(@Valid @RequestBody TechnicalVerificationRequest request) {
        CreateTechnicalVerificationCommand cmd = new CreateTechnicalVerificationCommand(
                request.getDescription(), request.getVerificationType());
        TechnicalVerification created = servicePort.save(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toTechnicalVerificationResponse(created));
    }

    @Operation(
            summary = "Actualizar verificación técnica",
            description = "Actualiza la información de una verificación técnica existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<TechnicalVerificationResponse> update(
            @Parameter(description = "Identificador único de la verificación técnica a actualizar", required = true)
            @PathVariable String id, @Valid @RequestBody TechnicalVerificationRequest request) {
        UpdateTechnicalVerificationCommand cmd = new UpdateTechnicalVerificationCommand(
                request.getDescription(), request.getVerificationType());
        TechnicalVerification updated = servicePort.update(id, cmd);
        return ResponseEntity.ok(restMapper.toTechnicalVerificationResponse(updated));
    }

    @Operation(
            summary = "Actualizar parcialmente una verificación técnica",
            description = "Actualiza únicamente los campos enviados de una verificación técnica existente. Los campos no enviados conservan su valor actual.")
    @PatchMapping("/{id}")
    public ResponseEntity<TechnicalVerificationResponse> patchTechnicalVerification(
            @Parameter(description = "Identificador único de la verificación técnica a actualizar", required = true)
            @PathVariable String id,
            @RequestBody TechnicalVerificationPatchRequest request) {
        TechnicalVerificationPatchCommand command = new TechnicalVerificationPatchCommand(
                request.getDescription(), request.getVerificationType());
        TechnicalVerification updated = servicePort.patchUpdate(id, command);
        return ResponseEntity.ok(restMapper.toTechnicalVerificationResponse(updated));
    }

    @Operation(
            summary = "Eliminar verificación técnica",
            description = "Elimina una verificación técnica del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único de la verificación técnica a eliminar", required = true)
            @PathVariable String id) {
        servicePort.delete(new DeleteTechnicalVerificationCommand(id));
        return ResponseEntity.noContent().build();
    }
}
