package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.ModelServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.ModelPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.CreateModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.DeleteModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.model_services.commands.UpdateModelCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.Model;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper.ModelRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.ModelPatchRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.ModelRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.ModelResponse;
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
@RequestMapping("/v1/api/models")
@Tag(name = "Model REST API", description = "Endpoints para la gestión de Model")
public class ModelController {
    private final ModelServicePort modelServicePort;
    private final ModelRestMapper modelRestMapper;

    @Autowired
    public ModelController(ModelServicePort modelServicePort, ModelRestMapper modelRestMapper) {
        this.modelServicePort = modelServicePort;
        this.modelRestMapper = modelRestMapper;
    }

    @Operation(
            summary = "Obtener todos los modelos",
            description = "Recupera la lista completa de modelos registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<ModelResponse>> getAllModels() {
        List<ModelResponse> response = modelRestMapper
                .toModelResponseList(modelServicePort.findAll());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener modelo por ID",
            description = "Recupera la información de un modelo específico utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse> getModel(
            @Parameter(description = "Identificador único del modelo", required = true)
            @PathVariable String id) {
        ModelResponse response = modelRestMapper
                .toModelResponse(modelServicePort.findById(id));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear nuevo modelo",
            description = "Crea un nuevo modelo en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<ModelResponse> createModel(@Valid @RequestBody ModelRequest request) {
        CreateModelCommand command = new CreateModelCommand(
                request.getInvima(), request.getManufacturerId(), request.getEquipmentId());
        Model created = modelServicePort.save(command);
        ModelResponse response = modelRestMapper.toModelResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Actualizar modelo",
            description = "Actualiza la información de un modelo existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<ModelResponse> updateModel(
            @Parameter(description = "Identificador único del modelo a actualizar", required = true)
            @PathVariable String id,
            @Valid @RequestBody ModelRequest request) {
        UpdateModelCommand command = new UpdateModelCommand(
                request.getInvima(), request.getManufacturerId(), request.getEquipmentId());
        Model updated = modelServicePort.update(id, command);
        ModelResponse response = modelRestMapper.toModelResponse(updated);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar parcialmente un modelo",
            description = "Actualiza únicamente los campos enviados de un modelo existente. Los campos no enviados conservan su valor actual.")
    @PatchMapping("/{id}")
    public ResponseEntity<ModelResponse> patchModel(
            @Parameter(description = "Identificador único del modelo a actualizar", required = true)
            @PathVariable String id,
            @RequestBody ModelPatchRequest request) {
        ModelPatchCommand command = new ModelPatchCommand(
                request.getInvima(), request.getManufacturerId(), request.getEquipmentId());
        Model updated = modelServicePort.patchUpdate(id, command);
        return ResponseEntity.ok(modelRestMapper.toModelResponse(updated));
    }

    @Operation(
            summary = "Eliminar modelo",
            description = "Elimina un modelo del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(
            @Parameter(description = "Identificador único del modelo a eliminar", required = true)
            @PathVariable String id) {
        DeleteModelCommand command = new DeleteModelCommand(id);
        modelServicePort.delete(command);
        return ResponseEntity.noContent().build();
    }
}
