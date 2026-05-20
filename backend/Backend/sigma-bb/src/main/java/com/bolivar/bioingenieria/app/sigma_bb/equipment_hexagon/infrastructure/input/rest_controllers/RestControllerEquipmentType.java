package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.EquipmentTypeServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.CreateEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.DeleteEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_type_services.commands.UpdateEquipmentTypeCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.metrological_data_services.commands.MetrologicalDataCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper.EquipmentTypeRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.EquipmentTypeRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.MetrologicalDataRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.UpdateMetrologicalDataBody;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.UpdateMetrologicalDataListBody;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.UpdateTechnicalVerificationBody;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.UpdateTechnicalVerificationListBody;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.EquipmentTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/equipment-types")
@Tag(name = "EquipmentType REST API", description = "Endpoints para la gestión de EquipmentType")
public class RestControllerEquipmentType {
    private final EquipmentTypeServicePort equipmentTypeServicePort;
    private final EquipmentTypeRestMapper equipmentTypeRestMapper;

    @Autowired
    public RestControllerEquipmentType(EquipmentTypeServicePort equipmentTypeServicePort,
                                       EquipmentTypeRestMapper equipmentTypeRestMapper) {
        this.equipmentTypeServicePort = equipmentTypeServicePort;
        this.equipmentTypeRestMapper = equipmentTypeRestMapper;
    }

    @Operation(
            summary = "Obtener todos los tipos de equipo",
            description = "Recupera la lista completa de tipos de equipo registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<EquipmentTypeResponse>> getAllEquipmentTypes() {
        return ResponseEntity.ok(equipmentTypeRestMapper
                .toEquipmentTypeResponseList(equipmentTypeServicePort.findAll()));
    }

    @Operation(
            summary = "Obtener tipo de equipo por ID",
            description = "Recupera la información de un tipo de equipo específico utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentTypeResponse> getEquipmentType(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id) {
        return ResponseEntity.ok(equipmentTypeRestMapper
                .toEquipmentTypeResponse(equipmentTypeServicePort.findById(id)));
    }

    @Operation(
            summary = "Crear nuevo tipo de equipo",
            description = "Crea un nuevo tipo de equipo en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<EquipmentTypeResponse> createEquipmentType(
            @Valid @RequestBody EquipmentTypeRequest request) {
        CreateEquipmentTypeCommand command = new CreateEquipmentTypeCommand(
                request.getEquipmentTypeName(), request.getTechnicalDefinition(),
                request.getCareRecommendations(), request.getVoltage(), request.getAmperage(),
                request.getPredominantTechnology(), request.getVerifiable(), request.getUnitMaintenanceValue(),
                equipmentTypeRestMapper.toEquipmentType(request).getMetrologicalData());
        EquipmentType created = equipmentTypeServicePort.save(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipmentTypeRestMapper.toEquipmentTypeResponse(created));
    }

    @Operation(
            summary = "Actualizar tipo de equipo",
            description = "Actualiza la información de un tipo de equipo existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentTypeResponse> updateEquipmentType(
            @Parameter(description = "Identificador único del tipo de equipo a actualizar", required = true)
            @PathVariable String id,
            @Valid @RequestBody EquipmentTypeRequest request) {
        UpdateEquipmentTypeCommand command = new UpdateEquipmentTypeCommand(
                request.getEquipmentTypeName(), request.getTechnicalDefinition(),
                request.getCareRecommendations(), request.getVoltage(), request.getAmperage(),
                request.getPredominantTechnology(), request.getVerifiable(), request.getUnitMaintenanceValue());
        EquipmentType updated = equipmentTypeServicePort.update(id, command);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(updated));
    }

    @Operation(
            summary = "Eliminar tipo de equipo",
            description = "Elimina un tipo de equipo del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipmentType(
            @Parameter(description = "Identificador único del tipo de equipo a eliminar", required = true)
            @PathVariable String id) {
        equipmentTypeServicePort.delete(new DeleteEquipmentTypeCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Agregar dato metrológico a un tipo de equipo",
            description = "Agrega un nuevo dato metrológico (valor y tipo) a un tipo de equipo existente.")
    @PostMapping("/{id}/metrological-data")
    public ResponseEntity<EquipmentTypeResponse> addMetrologicalData(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @Valid @RequestBody MetrologicalDataRequest request) {
        MetrologicalDataCommand cmd = new MetrologicalDataCommand(request.getValue(), request.getType());
        EquipmentType result = equipmentTypeServicePort.addMetrologicalData(id, cmd);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Eliminar dato metrológico de un tipo de equipo",
            description = "Elimina un dato metrológico específico de un tipo de equipo.")
    @DeleteMapping("/{id}/metrological-data")
    public ResponseEntity<EquipmentTypeResponse> removeMetrologicalData(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @Valid @RequestBody MetrologicalDataRequest request) {
        MetrologicalDataCommand cmd = new MetrologicalDataCommand(request.getValue(), request.getType());
        EquipmentType result = equipmentTypeServicePort.removeMetrologicalData(id, cmd);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Actualizar dato metrológico de un tipo de equipo",
            description = "Actualiza un dato metrológico específico de un tipo de equipo, reemplazando el valor anterior por el nuevo.")
    @PutMapping("/{id}/metrological-data")
    public ResponseEntity<EquipmentTypeResponse> updateMetrologicalData(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateMetrologicalDataBody body) {
        MetrologicalDataCommand oldCmd = new MetrologicalDataCommand(
                body.getOldData().getValue(), body.getOldData().getType());
        MetrologicalDataCommand newCmd = new MetrologicalDataCommand(
                body.getNewData().getValue(), body.getNewData().getType());
        EquipmentType result = equipmentTypeServicePort.updateMetrologicalData(id, oldCmd, newCmd);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Agregar lista de datos metrológicos a un tipo de equipo",
            description = "Agrega múltiples datos metrológicos en lote a un tipo de equipo existente.")
    @PostMapping("/{id}/metrological-data/batch")
    public ResponseEntity<EquipmentTypeResponse> addMetrologicalDataList(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @Valid @RequestBody List<MetrologicalDataRequest> requests) {
        List<MetrologicalDataCommand> cmds = requests.stream()
                .map(r -> new MetrologicalDataCommand(r.getValue(), r.getType())).toList();
        EquipmentType result = equipmentTypeServicePort.addMetrologicalDataList(id, cmds);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Eliminar lista de datos metrológicos de un tipo de equipo",
            description = "Elimina múltiples datos metrológicos en lote de un tipo de equipo.")
    @DeleteMapping("/{id}/metrological-data/batch")
    public ResponseEntity<EquipmentTypeResponse> removeMetrologicalDataList(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @Valid @RequestBody List<MetrologicalDataRequest> requests) {
        List<MetrologicalDataCommand> cmds = requests.stream()
                .map(r -> new MetrologicalDataCommand(r.getValue(), r.getType())).toList();
        EquipmentType result = equipmentTypeServicePort.removeMetrologicalDataList(id, cmds);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Actualizar lista de datos metrológicos de un tipo de equipo",
            description = "Actualiza múltiples datos metrológicos en lote de un tipo de equipo, reemplazando los valores anteriores por los nuevos.")
    @PutMapping("/{id}/metrological-data/batch")
    public ResponseEntity<EquipmentTypeResponse> updateMetrologicalDataList(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateMetrologicalDataListBody body) {
        List<MetrologicalDataCommand> oldCmds = body.getOldData().stream()
                .map(r -> new MetrologicalDataCommand(r.getValue(), r.getType())).toList();
        List<MetrologicalDataCommand> newCmds = body.getNewData().stream()
                .map(r -> new MetrologicalDataCommand(r.getValue(), r.getType())).toList();
        EquipmentType result = equipmentTypeServicePort.updateMetrologicalDataList(id, oldCmds, newCmds);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    // -------------------------------------------------------
    // ---------- TECHNICAL VERIFICATION ENDPOINTS ------------
    // -------------------------------------------------------

    @Operation(
            summary = "Agregar verificación técnica a un tipo de equipo",
            description = "Asocia una verificación técnica existente a un tipo de equipo mediante su UUID.")
        @PostMapping("/{id}/technical-verification")
    public ResponseEntity<EquipmentTypeResponse> addTechnicalVerification(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @RequestBody UUID technicalVerificationId) {
        EquipmentType result = equipmentTypeServicePort.addTechicalVerificationId(id, technicalVerificationId);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Eliminar verificación técnica de un tipo de equipo",
            description = "Desasocia una verificación técnica específica de un tipo de equipo.")
    @DeleteMapping("/{id}/technical-verification")
    public ResponseEntity<EquipmentTypeResponse> removeTechnicalVerification(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @RequestBody UUID technicalVerificationId) {
        EquipmentType result = equipmentTypeServicePort.removeTechicalVerificationId(id, technicalVerificationId);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Actualizar verificación técnica de un tipo de equipo",
            description = "Reemplaza una verificación técnica asociada a un tipo de equipo por otra diferente.")
    @PutMapping("/{id}/technical-verification")
    public ResponseEntity<EquipmentTypeResponse> updateTechnicalVerification(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateTechnicalVerificationBody body) {
        EquipmentType result = equipmentTypeServicePort.updateTechicalVerificationId(
                id, body.getOldTechnicalVerificationId(), body.getNewTechnicalVerificationId());
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Agregar lista de verificaciones técnicas a un tipo de equipo",
            description = "Asocia múltiples verificaciones técnicas en lote a un tipo de equipo existente.")
    @PostMapping("/{id}/technical-verification/batch")
    public ResponseEntity<EquipmentTypeResponse> addTechnicalVerificationList(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @RequestBody Set<UUID> technicalVerificationIds) {
        EquipmentType result = equipmentTypeServicePort.addTechicalVerificationIdList(id, technicalVerificationIds);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Eliminar lista de verificaciones técnicas de un tipo de equipo",
            description = "Desasocia múltiples verificaciones técnicas en lote de un tipo de equipo.")
    @DeleteMapping("/{id}/technical-verification/batch")
    public ResponseEntity<EquipmentTypeResponse> removeTechnicalVerificationList(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @RequestBody Set<UUID> technicalVerificationIds) {
        EquipmentType result = equipmentTypeServicePort.removeTechicalVerificationIdList(id, technicalVerificationIds);
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }

    @Operation(
            summary = "Actualizar lista de verificaciones técnicas de un tipo de equipo",
            description = "Reemplaza múltiples verificaciones técnicas asociadas a un tipo de equipo por otras diferentes en lote.")
    @PutMapping("/{id}/technical-verification/batch")
    public ResponseEntity<EquipmentTypeResponse> updateTechnicalVerificationList(
            @Parameter(description = "Identificador único del tipo de equipo", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateTechnicalVerificationListBody body) {
        EquipmentType result = equipmentTypeServicePort.updateTechicalVerificationIdList(
                id, body.getOldTechnicalVerificationIds(), body.getNewTechnicalVerificationIds());
        return ResponseEntity.ok(equipmentTypeRestMapper.toEquipmentTypeResponse(result));
    }
}
