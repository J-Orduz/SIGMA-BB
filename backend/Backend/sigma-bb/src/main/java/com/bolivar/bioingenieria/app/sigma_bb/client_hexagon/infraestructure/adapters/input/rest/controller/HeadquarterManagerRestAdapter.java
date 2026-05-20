package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.HeadquarterServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.HeadquarterRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ManagerRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.manager_request.ManagerCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.headquarter_response.HeadquarterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Adaptador REST para la gestión de gerentes de sedes.
 *
 * Esta clase expone los endpoints HTTP relacionados con la asociación de gerentes a sedes,
 * actuando como punto de entrada desde el exterior hacia la aplicación.
 *
 * Se encarga de:
 * - Recibir y validar las solicitudes HTTP.
 * - Delegar la lógica de negocio al servicio de dominio (puertos).
 * - Transformar los datos entre DTOs de entrada/salida y modelos de dominio.
 *
 * Sigue el principio de arquitectura hexagonal, donde este adaptador
 * pertenece a la capa de entrada (inbound).
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/headquarter/manager")
@Tag(
        name = "Headquarter Manager REST API",
        description = "Endpoints para la gestión de sedes por parte del gerente de cliente")
public class HeadquarterManagerRestAdapter {

    private final HeadquarterServicePort headquarterServicePort;

    private final HeadquarterRestMapper headquarterRestMapper;
    private final ManagerRestMapper managerRestMapper;

    // --------------------------------------------------------
    // ------------ CRUD MANAGER HEADQUARTER ------------------
    // --------------------------------------------------------

    /**
     * Agrega un gerente a una sede existente.
     *
     * Este endpoint recibe el identificador de la sede y los datos del gerente a agregar,
     * los valida y los transforma a un modelo de dominio para asociarlo a la sede.
     * Luego, retorna la información actualizada de la sede con el nuevo gerente asociado.
     *
     * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto con la información actualizada de la sede.
     *
     * @param idHeadquarter Identificador único de la sede a actualizar.
     * @param request Datos del gerente a agregar.
     * @return ResponseEntity con la información actualizada de la sede y estado HTTP 200 (OK).
     */
    @Operation(
            summary = "Agregar un gerente a una sede",
            description = "Este endpoint permite agregar un gerente a una sede existente. " +
                    "Recibe el identificador de la sede y los datos del gerente a agregar, " +
                    "y retorna la información actualizada de la sede con el nuevo gerente asociado.")
    @PutMapping("/v1/api/{idHeadquarter}")
    public ResponseEntity<HeadquarterResponse> createHeadquarterManager(
            @Parameter(
                    description = "Identificador único de la sede a actualizar",
                    required = true
            )
            @PathVariable UUID idHeadquarter,
            @Valid @RequestBody ManagerCreateRequest request) {
        return ResponseEntity.ok(
                headquarterRestMapper.toHeadquarterResponse(
                        headquarterServicePort.addManger(
                                idHeadquarter,
                                managerRestMapper.toManager(request))));
    }

    /**
     * Actualiza el gerente de una sede existente.
     *
     * Este endpoint recibe el identificador de la sede, el identificador del gerente a actualizar y los nuevos datos del gerente,
     * los valida y los transforma a un modelo de dominio para actualizar la asociación al gerente en la sede.
     * Luego, retorna la información actualizada de la sede con el gerente modificado.
     *
     * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto con la información actualizada de la sede.
     *
     * @param idHeadquarter Identificador único de la sede a actualizar.
     * @param idManager Identificador único del gerente a actualizar.
     * @param request Datos del gerente a actualizar.
     * @return ResponseEntity con la información actualizada de la sede y estado HTTP 200 (OK).
     */
    @Operation(
            summary = "Actualizar el gerente de una sede",
            description = "Este endpoint permite actualizar el gerente de una sede existente. " +
                    "Recibe el identificador de la sede, el identificador del gerente a actualizar y los nuevos datos del gerente, " +
                    "y retorna la información actualizada de la sede con el gerente modificado.")
    @PutMapping("/v1/api/{idHeadquarter}/{idManager}")
    public ResponseEntity<HeadquarterResponse> updateHeadquarterManager(
            @Parameter(
                    description = "Identificador único de la sede a actualizar",
                    required = true
            )
            @PathVariable UUID idHeadquarter,
            @Parameter(
                    description = "Identificador único del gerente a actualizar",
                    required = true
            )
            @PathVariable UUID idManager,
            @Valid @RequestBody ManagerCreateRequest request) {
        return ResponseEntity.ok(
                headquarterRestMapper.toHeadquarterResponse(
                        headquarterServicePort.updateManger(
                                idHeadquarter,
                                idManager,
                                managerRestMapper.toManager(request))));
    }

    /**
     * Elimina un gerente de una sede existente.
     *
     * Este endpoint recibe el identificador de la sede y el identificador del gerente a eliminar,
     * y ejecuta la operación de eliminación en el servicio de dominio. Luego, retorna la información actualizada de la sede sin el gerente asociado.
     *
     * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto con la información actualizada de la sede.
     *
     * @param idHeadquarter Identificador único de la sede a actualizar.
     * @param idManager Identificador único del gerente a eliminar.
     * @return ResponseEntity con la información actualizada de la sede y estado HTTP 200 (OK).
     */
    @Operation(
            summary = "Eliminar un gerente de una sede",
            description = "Este endpoint permite eliminar un gerente asociado a una sede existente. " +
                    "Recibe el identificador de la sede y el identificador del gerente a eliminar, " +
                    "y retorna la información actualizada de la sede sin el gerente asociado.")
    @DeleteMapping("/v1/api/{idHeadquarter}/{idManager}")
    public ResponseEntity<HeadquarterResponse> deleteHeadquarterManager(
            @Parameter(
                    description = "Identificador único de la sede a actualizar",
                    required = true)
            @PathVariable UUID idHeadquarter,
            @Parameter(
                    description = "Identificador único del gerente a eliminar",
                    required = true)
            @PathVariable UUID idManager) {
        return ResponseEntity.ok(
                headquarterRestMapper.toHeadquarterResponse(
                    headquarterServicePort.deleteManger(idHeadquarter, idManager)));
    }
}
