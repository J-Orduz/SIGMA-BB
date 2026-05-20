package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.ManufacturerServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.CreateManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.DeleteManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.manufacturer_services.commands.UpdateManufacturerCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.Manufacturer;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper.ManufacturerRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.ManufacturerRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.ManufacturerResponse;
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
@RequestMapping("/v1/api/manufacturers")
@Tag(name = "Manufacturer REST API", description = "Endpoints para la gestión de Manufacturer")
public class RestControllerManufacturer {
    private final ManufacturerServicePort manufacturerServicePort;
    private final ManufacturerRestMapper manufacturerRestMapper;

    @Autowired
    public RestControllerManufacturer(ManufacturerServicePort manufacturerServicePort, ManufacturerRestMapper manufacturerRestMapper) {
        this.manufacturerServicePort = manufacturerServicePort;
        this.manufacturerRestMapper = manufacturerRestMapper;
    }

    @Operation(
            summary = "Obtener todos los fabricantes",
            description = "Recupera la lista completa de fabricantes registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<ManufacturerResponse>> getAllManufacturers() {
        List<ManufacturerResponse> response = manufacturerRestMapper
                .toManufacturerResponseList(manufacturerServicePort.findAll());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener fabricante por ID",
            description = "Recupera la información de un fabricante específico utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponse> getManufacturer(
            @Parameter(description = "Identificador único del fabricante", required = true)
            @PathVariable String id) {
        ManufacturerResponse response = manufacturerRestMapper
                .toManufacturerResponse(manufacturerServicePort.findById(id));

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear nuevo fabricante",
            description = "Crea un nuevo fabricante en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<ManufacturerResponse> createManufacturer(
            @Valid @RequestBody ManufacturerRequest request) {

        CreateManufacturerCommand command = new CreateManufacturerCommand(request.getName(), request.getCountryId());
        Manufacturer created = manufacturerServicePort.save(command);

        ManufacturerResponse response = manufacturerRestMapper.toManufacturerResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Actualizar fabricante",
            description = "Actualiza la información de un fabricante existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerResponse> updateManufacturer(
            @Parameter(description = "Identificador único del fabricante a actualizar", required = true)
            @PathVariable String id,
            @Valid @RequestBody ManufacturerRequest request) {

        UpdateManufacturerCommand command = new UpdateManufacturerCommand(request.getName(), request.getCountryId());
        Manufacturer updated = manufacturerServicePort.update(id, command);

        ManufacturerResponse response = manufacturerRestMapper.toManufacturerResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar fabricante",
            description = "Elimina un fabricante del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(
            @Parameter(description = "Identificador único del fabricante a eliminar", required = true)
            @PathVariable String id) {
        DeleteManufacturerCommand command = new DeleteManufacturerCommand(id);
        manufacturerServicePort.delete(command);
        return ResponseEntity.noContent().build();
    }
}
