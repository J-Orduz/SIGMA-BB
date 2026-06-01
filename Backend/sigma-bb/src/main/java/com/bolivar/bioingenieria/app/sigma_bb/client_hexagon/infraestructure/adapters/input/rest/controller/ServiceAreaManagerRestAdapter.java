package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ServiceAreaServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ManagerRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ServiceAreaRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.manager_request.ManagerCreateRequest;
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
 * Adaptador REST para la gestión de gerentes de áreas de servicio.
 *
 * Esta clase expone los endpoints HTTP relacionados con la asociación de
 * gerentes a áreas de servicio,
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
@RequestMapping("/service-area/manager")
@Tag(name = "Service Area Manager REST API", description = "Endpoints para la gestión de areas de servicio por parte del gerente de cliente")
public class ServiceAreaManagerRestAdapter {

        private final ServiceAreaServicePort serviceAreaServicePort;

        private final ServiceAreaRestMapper serviceAreaRestMapper;
        private final ManagerRestMapper managerRestMapper;

        // --------------------------------------------------------
        // ------------ CRUD MANAGER HEADQUARTER ------------------
        // --------------------------------------------------------

        /**
         * Agrega un gerente a un area de servicio existente.
         *
         * Este endpoint recibe el identificador del area de servicio y los datos del
         * gerente a agregar,
         * los valida y los transforma a un modelo de dominio para asociarlo al area de
         * servicio.
         * Luego, retorna la información actualizada del area de servicio con el nuevo
         * gerente asociado.
         *
         * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto
         * con la información actualizada del area de servicio.
         *
         * @param idServiceArea Identificador único del area de servicio a actualizar.
         * @param request       Datos del gerente a agregar.
         * @return ResponseEntity con la información actualizada del area de servicio y
         *         estado HTTP 200 (OK).
         */
        @Operation(summary = "Agregar un gerente a un area de servicio", description = "Este endpoint permite agregar un gerente a un area de servicio existente. "
                        +
                        "Recibe el identificador del area de servicio y los datos del gerente a agregar, " +
                        "y retorna la información actualizada del area de servicio con el nuevo gerente asociado.")
        @PutMapping("/v1/api/{idServiceArea}")
        public ResponseEntity<ServiceAreaResponse> createServiceAreaManager(
                        @Parameter(description = "Identificador único del area de servicio a actualizar", required = true) @PathVariable UUID idServiceArea,
                        @Valid @RequestBody ManagerCreateRequest request) {
                return ResponseEntity.ok(
                                serviceAreaRestMapper.toServiceAreaResponse(
                                                serviceAreaServicePort.addManger(
                                                                idServiceArea,
                                                                managerRestMapper.toManager(request))));
        }

        /**
         * Actualiza un gerente de un area de servicio existente.
         *
         * Este endpoint recibe el identificador del area de servicio, el identificador
         * del gerente a actualizar y los nuevos datos del gerente,
         * los valida y los transforma a un modelo de dominio para actualizar la
         * asociación al area de servicio.
         * Luego, retorna la información actualizada del area de servicio con el gerente
         * actualizado asociado.
         *
         * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto
         * con la información actualizada del area de servicio.
         *
         * @param idServiceArea Identificador único del area de servicio a actualizar.
         * @param idManager     Identificador único del gerente a actualizar.
         * @param request       Datos del gerente a actualizar.
         * @return ResponseEntity con la información actualizada del area de servicio y
         *         estado HTTP 200 (OK).
         */
        @Operation(summary = "Actualizar un gerente de un area de servicio", description = "Este endpoint permite actualizar un gerente asociado a un area de servicio existente. "
                        +
                        "Recibe el identificador del area de servicio, el identificador del gerente a actualizar y los nuevos datos del gerente, "
                        +
                        "y retorna la información actualizada del area de servicio con el gerente actualizado asociado.")
        @PutMapping("/v1/api/{idServiceArea}/{idManager}")
        public ResponseEntity<ServiceAreaResponse> updateServiceAreaManager(
                        @Parameter(description = "Identificador único del area de servicio a actualizar", required = true) @PathVariable UUID idServiceArea,
                        @Parameter(description = "Identificador único del gerente a actualizar", required = true) @PathVariable UUID idManager,
                        @Valid @RequestBody ManagerCreateRequest request) {
                return ResponseEntity.ok(
                                serviceAreaRestMapper.toServiceAreaResponse(
                                                serviceAreaServicePort.updateManger(
                                                                idServiceArea,
                                                                idManager,
                                                                managerRestMapper.toManager(request))));
        }

        /**
         * Elimina un gerente de un area de servicio existente.
         *
         * Este endpoint recibe el identificador del area de servicio y el identificador
         * del gerente a eliminar,
         * ejecuta la operación de eliminación en el servicio de dominio, y retorna la
         * información actualizada del area de servicio sin el gerente asociado.
         *
         * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto
         * con la información actualizada del area de servicio.
         *
         * @param idServiceArea Identificador único del area de servicio a actualizar.
         * @param idManager     Identificador único del gerente a eliminar.
         * @return ResponseEntity con la información actualizada del area de servicio y
         *         estado HTTP 200 (OK).
         */
        @Operation(summary = "Eliminar un gerente de un area de servicio", description = "Este endpoint permite eliminar un gerente asociado a un area de servicio existente. "
                        +
                        "Recibe el identificador del area de servicio y el identificador del gerente a eliminar, " +
                        "y retorna la información actualizada del area de servicio sin el gerente asociado.")
        @DeleteMapping("/v1/api/{idServiceArea}/{idManager}")
        public ResponseEntity<ServiceAreaResponse> deleteHeadquarterManager(
                        @Parameter(description = "Identificador único del area de servicio a actualizar", required = true) @PathVariable UUID idServiceArea,
                        @Parameter(description = "Identificador único del gerente a eliminar", required = true) @PathVariable UUID idManager) {
                return ResponseEntity.ok(
                                serviceAreaRestMapper.toServiceAreaResponse(
                                                serviceAreaServicePort.deleteManger(idServiceArea, idManager)));
        }
}
