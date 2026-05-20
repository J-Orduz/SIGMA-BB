package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.EquipmentServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.CreateEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.DeleteEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.equipment_services.commands.UpdateEquipmentCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.Equipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper.EquipmentRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.EquipmentRequest;
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
    private final EquipmentServicePort servicePort;
    private final EquipmentRestMapper restMapper;

    @Autowired
    public RestControllerEquipment(EquipmentServicePort servicePort,
                                   EquipmentRestMapper restMapper) {
        this.servicePort = servicePort;
        this.restMapper = restMapper;
    }

    @Operation(
            summary = "Obtener todos los equipos",
            description = "Recupera la lista completa de equipos registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<EquipmentReponse>> getAll() {
        return ResponseEntity.ok(restMapper.toEquipmentResponseList(servicePort.findAll()));
    }

    @Operation(
            summary = "Obtener equipo por ID",
            description = "Recupera la información de un equipo específico utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentReponse> getById(
            @Parameter(description = "Identificador único del equipo", required = true)
            @PathVariable String id) {
        return ResponseEntity.ok(restMapper.toEquipmentResponse(servicePort.findById(id)));
    }

    @Operation(
            summary = "Crear nuevo equipo",
            description = "Crea un nuevo equipo en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<EquipmentReponse> create(@Valid @RequestBody EquipmentRequest request) {
        CreateEquipmentCommand cmd = new CreateEquipmentCommand(
                request.getEquipmentTypeId(), request.getBrandId());
        Equipment created = servicePort.save(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toEquipmentResponse(created));
    }

    @Operation(
            summary = "Actualizar equipo",
            description = "Actualiza la información de un equipo existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentReponse> update(
            @Parameter(description = "Identificador único del equipo a actualizar", required = true)
            @PathVariable String id, @Valid @RequestBody EquipmentRequest request) {
        UpdateEquipmentCommand cmd = new UpdateEquipmentCommand(
                request.getEquipmentTypeId(), request.getBrandId());
        Equipment updated = servicePort.update(id, cmd);
        return ResponseEntity.ok(restMapper.toEquipmentResponse(updated));
    }

    @Operation(
            summary = "Eliminar equipo",
            description = "Elimina un equipo del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único del equipo a eliminar", required = true)
            @PathVariable String id) {
        servicePort.delete(new DeleteEquipmentCommand(id));
        return ResponseEntity.noContent().build();
    }
}
