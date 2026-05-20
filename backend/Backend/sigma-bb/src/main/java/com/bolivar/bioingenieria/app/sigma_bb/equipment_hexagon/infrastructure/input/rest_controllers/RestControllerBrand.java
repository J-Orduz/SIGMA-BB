package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.input.BrandServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.CreateBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.DeleteBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.brand_services.commands.UpdateBrandCommand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper.BrandRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.BrandRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.BrandResponse;
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
@RequestMapping("/v1/api/brands")
@Tag(name = "Brand REST API", description = "Endpoints para la gestión de Brand")
public class RestControllerBrand {
    private final BrandServicePort brandServicePort;
    private final BrandRestMapper brandRestMapper;

    @Autowired
    public RestControllerBrand(BrandServicePort brandServicePort, BrandRestMapper brandRestMapper) {
        this.brandServicePort = brandServicePort;
        this.brandRestMapper = brandRestMapper;
    }

    @Operation(
            summary = "Obtener todas las marcas",
            description = "Recupera la lista completa de marcas registradas en el sistema.")
    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        List<BrandResponse> response = brandRestMapper
                .toBrandResponseList(brandServicePort.findAll());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener marca por ID",
            description = "Recupera la información de una marca específica utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrand(
            @Parameter(description = "Identificador único de la marca", required = true)
            @PathVariable String id) {
        BrandResponse response = brandRestMapper
                .toBrandResponse(brandServicePort.findById(id));

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear nueva marca",
            description = "Crea una nueva marca en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(
            @Valid @RequestBody BrandRequest request) {

        CreateBrandCommand command = new CreateBrandCommand(request.getName());
        Brand created = brandServicePort.save(command);

        BrandResponse response = brandRestMapper.toBrandResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Actualizar marca",
            description = "Actualiza la información de una marca existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> updateBrand(
            @Parameter(description = "Identificador único de la marca a actualizar", required = true)
            @PathVariable String id,
            @Valid @RequestBody BrandRequest request) {

        UpdateBrandCommand command = new UpdateBrandCommand(request.getName());
        Brand updated = brandServicePort.update(id, command);

        BrandResponse response = brandRestMapper.toBrandResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar marca",
            description = "Elimina una marca del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(
            @Parameter(description = "Identificador único de la marca a eliminar", required = true)
            @PathVariable String id) {
        DeleteBrandCommand command = new DeleteBrandCommand(id);
        brandServicePort.delete(command);
        return ResponseEntity.noContent().build();
    }
}
