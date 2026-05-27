package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.input.CityServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.CityPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.CreateCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.DeleteCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.UpdateCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.City;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.mapper.CityRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.request.CityPatchRequest;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.request.CityRequest;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.response.CityResponse;
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
@RequestMapping("/v1/api/cities")
@Tag(name = "City REST API", description = "Endpoints para la gestión de City")
public class RestControllerCity {
    private final CityServicePort cityServicePort;
    private final CityRestMapper cityRestMapper;

    @Autowired
    public RestControllerCity(CityServicePort cityServicePort,
                              CityRestMapper cityRestMapper) {
        this.cityServicePort = cityServicePort;
        this.cityRestMapper = cityRestMapper;
    }

    @Operation(
            summary = "Obtener todas las ciudades",
            description = "Recupera la lista completa de ciudades registradas en el sistema.")
    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {
        List<CityResponse> response = cityRestMapper
                .toCityResponseList(cityServicePort.findAll());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener ciudad por ID",
            description = "Recupera la información de una ciudad específica utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCity(
            @Parameter(description = "Identificador único de la ciudad", required = true)
            @PathVariable String id) {
        CityResponse response = cityRestMapper
                .toCityResponse(cityServicePort.findById(id));

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear nueva ciudad",
            description = "Crea una nueva ciudad en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<CityResponse> createCity(
            @Valid @RequestBody CityRequest request) {

        CreateCityCommand command = new CreateCityCommand(
                request.getName(),
                request.getCountryId()
        );

        City created = cityServicePort.save(command);

        CityResponse response = cityRestMapper.toCityResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Actualizar ciudad",
            description = "Actualiza la información de una ciudad existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> updateCity(
            @Parameter(description = "Identificador único de la ciudad a actualizar", required = true)
            @PathVariable String id,
            @Valid @RequestBody CityRequest request) {

        UpdateCityCommand command = new UpdateCityCommand(
                request.getId(),
                request.getName(),
                request.getCountryId()
        );

        City updated = cityServicePort.update(id, command);

        CityResponse response = cityRestMapper.toCityResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar parcialmente una ciudad",
            description = "Actualiza únicamente los campos enviados de una ciudad existente. Los campos no enviados conservan su valor actual.")
    @PatchMapping("/{id}")
    public ResponseEntity<CityResponse> patchCity(
            @Parameter(description = "Identificador único de la ciudad a actualizar", required = true)
            @PathVariable String id,
            @RequestBody CityPatchRequest request) {
        CityPatchCommand command = new CityPatchCommand(request.getId(), request.getName(), request.getCountryId());
        City updated = cityServicePort.patchUpdate(id, command);
        return ResponseEntity.ok(cityRestMapper.toCityResponse(updated));
    }

    @Operation(
            summary = "Eliminar ciudad",
            description = "Elimina una ciudad del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(
            @Parameter(description = "Identificador único de la ciudad a eliminar", required = true)
            @PathVariable String id) {
        DeleteCityCommand command = new DeleteCityCommand(id);
        cityServicePort.delete(command);
        return ResponseEntity.noContent().build();
    }
}
