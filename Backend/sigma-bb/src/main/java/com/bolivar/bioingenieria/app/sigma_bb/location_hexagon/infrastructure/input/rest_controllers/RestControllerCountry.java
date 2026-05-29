package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.rest_controllers;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.input.CountryServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.CountryPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.CreateCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.DeleteCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.UpdateCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.Country;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.mapper.CountryRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.request.CountryPatchRequest;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.request.CountryRequest;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.response.CountryResponse;
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
@RequestMapping("/v1/api/countries")
@Tag(name = "Country REST API", description = "Endpoints para la gestión de Country")
public class RestControllerCountry {
    private final CountryServicePort countryServicePort;
    private final CountryRestMapper countryRestMapper;

    @Autowired
    public RestControllerCountry(CountryServicePort countryServicePort,
                                  CountryRestMapper countryRestMapper) {
        this.countryServicePort = countryServicePort;
        this.countryRestMapper = countryRestMapper;
    }

    @Operation(
            summary = "Obtener todos los países",
            description = "Recupera la lista completa de países registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAllCountries() {
        List<CountryResponse> response = countryRestMapper
                .toCountryResponseList(countryServicePort.findAll());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener país por ID",
            description = "Recupera la información de un país específico utilizando su identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getCountry(
            @Parameter(description = "Identificador único del país", required = true)
            @PathVariable String id) {
        CountryResponse response = countryRestMapper
                .toCountryResponse(countryServicePort.findById(id));

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear nuevo país",
            description = "Crea un nuevo país en el sistema a partir de los datos proporcionados.")
    @PostMapping
    public ResponseEntity<CountryResponse> createCountry(
            @Valid @RequestBody CountryRequest request) {

        CreateCountryCommand command = new CreateCountryCommand(request.getId(), request.getName());
        Country created = countryServicePort.save(command);

        CountryResponse response = countryRestMapper.toCountryResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Actualizar país",
            description = "Actualiza la información de un país existente utilizando su identificador único y los nuevos datos proporcionados.")
    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> updateCountry(
            @Parameter(description = "Identificador único del país a actualizar", required = true)
            @PathVariable String id,
            @Valid @RequestBody CountryRequest request) {

        UpdateCountryCommand command = new UpdateCountryCommand(request.getId(), request.getName());
        Country updated = countryServicePort.update(id, command);

        CountryResponse response = countryRestMapper.toCountryResponse(updated);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar parcialmente un país",
            description = "Actualiza únicamente los campos enviados de un país existente. Los campos no enviados conservan su valor actual.")
    @PatchMapping("/{id}")
    public ResponseEntity<CountryResponse> patchCountry(
            @Parameter(description = "Identificador único del país a actualizar", required = true)
            @PathVariable String id,
            @RequestBody CountryPatchRequest request) {
        CountryPatchCommand command = new CountryPatchCommand(request.getId(), request.getName());
        Country updated = countryServicePort.patchUpdate(id, command);
        return ResponseEntity.ok(countryRestMapper.toCountryResponse(updated));
    }

    @Operation(
            summary = "Eliminar país",
            description = "Elimina un país del sistema utilizando su identificador único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(
            @Parameter(description = "Identificador único del país a eliminar", required = true)
            @PathVariable String id) {
        DeleteCountryCommand command = new DeleteCountryCommand(id);
        countryServicePort.delete(command);
        return ResponseEntity.noContent().build();
    }
}
