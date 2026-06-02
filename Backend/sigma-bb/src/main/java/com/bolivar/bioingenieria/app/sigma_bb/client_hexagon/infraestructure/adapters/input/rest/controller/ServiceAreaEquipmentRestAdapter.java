package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ServiceAreaServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ClientEquipmentRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ServiceAreaRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.clientEqupment_request.ClientEquipmentCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.serviceArea_response.ServiceAreaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Adaptador REST para la gestión de equipos de cliente asociados a áreas de servicio.
 * Expone endpoints para agregar, actualizar y eliminar equipos dentro de un área de servicio.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/service-area/equipment")
@Tag(name = "Service Area Equipment REST API", description = "Endpoints para la gestión de equipos asociados a áreas de servicio de clientes")
public class ServiceAreaEquipmentRestAdapter {

    private final ServiceAreaServicePort serviceAreaServicePort;
    private final ServiceAreaRestMapper serviceAreaRestMapper;
    private final ClientEquipmentRestMapper clientEquipmentRestMapper;

    /**
     * Agrega un equipo a un área de servicio existente.
     *
     * @param idServiceArea Identificador único del área de servicio
     * @param request       Datos del equipo a registrar
     * @return ResponseEntity con la información actualizada de la ServiceArea
     */
    @Operation(summary = "Agregar un equipo a un área de servicio", description = "Este endpoint permite agregar un nuevo equipo biomédico a un área de servicio existente.")
    @PutMapping("/v1/api/{idServiceArea}")
    public ResponseEntity<ServiceAreaResponse> addServiceAreaEquipment(
            @Parameter(description = "Identificador único del área de servicio", required = true) @PathVariable UUID idServiceArea,
            @Valid @RequestBody ClientEquipmentCreateRequest request) {
        return ResponseEntity.ok(
                serviceAreaRestMapper.toServiceAreaResponse(
                        serviceAreaServicePort.addClientEquipment(
                                idServiceArea,
                                clientEquipmentRestMapper.toClientEquipment(request))));
    }

    /**
     * Actualiza un equipo dentro de un área de servicio existente.
     *
     * @param idServiceArea Identificador único del área de servicio
     * @param idEquipment   Identificador único del equipo del cliente a actualizar
     * @param request       Nuevos datos del equipo
     * @return ResponseEntity con la información actualizada de la ServiceArea
     */
    @Operation(summary = "Actualizar un equipo de un área de servicio", description = "Este endpoint permite actualizar los detalles de un equipo existente dentro de un área de servicio.")
    @PutMapping("/v1/api/{idServiceArea}/{idEquipment}")
    public ResponseEntity<ServiceAreaResponse> updateServiceAreaEquipment(
            @Parameter(description = "Identificador único del área de servicio", required = true) @PathVariable UUID idServiceArea,
            @Parameter(description = "Identificador único del equipo a actualizar", required = true) @PathVariable UUID idEquipment,
            @Valid @RequestBody ClientEquipmentCreateRequest request) {
        return ResponseEntity.ok(
                serviceAreaRestMapper.toServiceAreaResponse(
                        serviceAreaServicePort.updateClientEquipment(
                                idServiceArea,
                                idEquipment,
                                clientEquipmentRestMapper.toClientEquipment(request))));
    }

    /**
     * Elimina (desactiva) un equipo de un área de servicio existente.
     *
     * @param idServiceArea Identificador único del área de servicio
     * @param idEquipment   Identificador único del equipo a eliminar
     * @return ResponseEntity con la información actualizada de la ServiceArea
     */
    @Operation(summary = "Eliminar un equipo de un área de servicio", description = "Este endpoint permite dar de baja (soft delete) un equipo dentro de un área de servicio.")
    @DeleteMapping("/v1/api/{idServiceArea}/{idEquipment}")
    public ResponseEntity<ServiceAreaResponse> deleteServiceAreaEquipment(
            @Parameter(description = "Identificador único del área de servicio", required = true) @PathVariable UUID idServiceArea,
            @Parameter(description = "Identificador único del equipo a eliminar", required = true) @PathVariable UUID idEquipment) {
        serviceAreaServicePort.deleteClientEquipment(idServiceArea, idEquipment);
        return ResponseEntity.ok(
                serviceAreaRestMapper.toServiceAreaResponse(
                        serviceAreaServicePort.findById(idServiceArea)));
    }
}
