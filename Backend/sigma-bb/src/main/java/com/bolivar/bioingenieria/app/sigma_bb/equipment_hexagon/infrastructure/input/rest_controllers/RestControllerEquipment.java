package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.EquipmentCommandServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.EquipmentServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.EquipmentPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.CreateEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.DeleteEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.UpdateEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.Equipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper.EquipmentRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.EquipmentPatchRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.EquipmentRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.EquipmentIdsResponse;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.EquipmentReponse;
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
@RequestMapping("/v1/api/equipment")
@Tag(name = "Equipment REST API", description = "Endpoints para la gestión de Equipment")
public class RestControllerEquipment {
    private final EquipmentServicePort queryServicePort;
    private final EquipmentCommandServicePort commandServicePort;
    private final EquipmentRestMapper restMapper;

    @Autowired
    public RestControllerEquipment(EquipmentServicePort queryServicePort,
                                   EquipmentCommandServicePort commandServicePort,
                                   EquipmentRestMapper restMapper) {
        this.queryServicePort = queryServicePort;
        this.commandServicePort = commandServicePort;
        this.restMapper = restMapper;
    }

    @Operation(
            summary = "Obtener todos los equipos",
            description = "Recupera la lista completa de equipos registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<EquipmentReponse>> getAll() {
        return ResponseEntity.ok(restMapper.toEquipmentResponseList(queryServicePort.findAll()));
    }

    @Operation(
            summary = "Obtener equipo por ID",
            description = "Recupera la información de un equipo específico utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentReponse> getById(
            @Parameter(description = "Identificador único del equipo", required = true)
            @PathVariable String id) {
        return ResponseEntity.ok(restMapper.toEquipmentResponse(queryServicePort.findById(id)));
    }

    @Operation(
            summary = "Crear nuevo equipo",
            description = "Crea un nuevo equipo en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<EquipmentIdsResponse> create(@Valid @RequestBody EquipmentRequest request) {
        CreateEquipmentCommand cmd = new CreateEquipmentCommand(
                request.getEquipmentTypeId(), request.getBrandId());
        Equipment created = commandServicePort.save(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toEquipmentIdsResponse(created));
    }

    @Operation(
            summary = "Actualizar equipo",
            description = "Actualiza la información de un equipo existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentIdsResponse> update(
            @Parameter(description = "Identificador único del equipo a actualizar", required = true)
            @PathVariable String id, @Valid @RequestBody EquipmentRequest request) {
        UpdateEquipmentCommand cmd = new UpdateEquipmentCommand(
                request.getEquipmentTypeId(), request.getBrandId());
        Equipment updated = commandServicePort.update(id, cmd);
        return ResponseEntity.ok(restMapper.toEquipmentIdsResponse(updated));
    }

    @Operation(
            summary = "Actualizar parcialmente un equipo",
            description = "Actualiza únicamente los campos enviados de un equipo existente. Los campos no enviados conservan su valor actual.")
    @PatchMapping("/{id}")
    public ResponseEntity<EquipmentIdsResponse> patchEquipment(
            @Parameter(description = "Identificador único del equipo a actualizar", required = true)
            @PathVariable String id,
            @RequestBody EquipmentPatchRequest request) {
        EquipmentPatchCommand command = new EquipmentPatchCommand(request.getEquipmentTypeId(), request.getBrandId());
        Equipment updated = commandServicePort.patchUpdate(id, command);
        return ResponseEntity.ok(restMapper.toEquipmentIdsResponse(updated));
    }

    @Operation(
            summary = "Eliminar equipo",
            description = "Elimina un equipo del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único del equipo a eliminar", required = true)
            @PathVariable String id) {
        commandServicePort.delete(new DeleteEquipmentCommand(id));
        return ResponseEntity.noContent().build();
    }
}
