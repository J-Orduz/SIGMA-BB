package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ServiceAreaServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ServiceAreaRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.serviceArea_request.ServiceAreaCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Adaptador REST para la gestión de áreas de servicio.
 *
 * Esta clase expone los endpoints HTTP relacionados con el recurso área de
 * servicio,
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
@RequestMapping("/service-area")
@Tag(name = "Service Area REST API", description = "Controlador REST para gestionar las áreas de servicio de las sedes de los clientes. "
                +
                "Permite realizar operaciones CRUD sobre las áreas de servicio, incluyendo la creación, actualización, eliminación y consulta de las mismas.")
public class ServiceAreaRestAdapter {

        private final ServiceAreaServicePort serviceAreaServicePort;

        private final ServiceAreaRestMapper serviceAreaRestMapper;

        // --------------------------------------------------------
        // ------------------ CRUD SERVICE AREA -------------------
        // --------------------------------------------------------

        /**
         * Obtiene la lista de todas las áreas de servicio registradas.
         *
         * Este endpoint consulta el servicio de dominio para recuperar
         * todas las áreas de servicio y las transforma a objetos de respuesta
         * mediante el mapper correspondiente.
         *
         * @return Lista de áreas de servicio en formato de respuesta (DTO)
         */
        @Operation(summary = "Obtener todas las áreas de servicio", description = "Este endpoint devuelve una lista de todas las áreas de servicio registradas en el sistema.")
        @GetMapping("/v1/api")
        public List<?> getAllServiceAreas() {
                return serviceAreaRestMapper.toServiceAreaResponseList(serviceAreaServicePort.findAll());
        }

        /**
         * Obtiene los detalles de un área de servicio específica por su ID.
         *
         * Este endpoint consulta el servicio de dominio para recuperar
         * el área de servicio correspondiente al ID proporcionado y la transforma
         * a un objeto de respuesta mediante el mapper correspondiente.
         *
         * @param serviceAreaId ID del área de servicio a consultar
         * @return Detalles del área de servicio en formato de respuesta (DTO)
         */
        @Operation(summary = "Obtener área de servicio por ID", description = "Este endpoint devuelve los detalles de un área de servicio específica identificada por su ID.")
        @GetMapping("/v1/api/{serviceAreaId}")
        public Object getServiceAreaById(
                        @Parameter(description = "ID del área de servicio a consultar", required = true) @PathVariable UUID serviceAreaId) {
                return serviceAreaRestMapper.toServiceAreaResponse(serviceAreaServicePort.findById(serviceAreaId));
        }

        /**
         * Crea una nueva área de servicio en el sistema.
         *
         * Este endpoint recibe los datos de un nuevo área de servicio en formato JSON,
         * los transforma a un modelo de dominio mediante el mapper correspondiente,
         * y luego delega la creación al servicio de dominio. Finalmente, devuelve la
         * información del área de servicio creada en formato de respuesta (DTO).
         *
         * @param serviceAreaCreateRequest DTO de entrada con los datos del área de
         *                                 servicio a crear
         * @return Información del área de servicio creada en formato de respuesta (DTO)
         */
        @Operation(summary = "Crear nueva área de servicio", description = "Este endpoint permite crear una nueva área de servicio en el sistema. "
                        +
                        "Recibe los datos del área de servicio en formato JSON y devuelve la información del área creada.")
        @PostMapping("/v1/api")
        public ResponseEntity<?> createServiceArea(
                        @Valid @RequestBody ServiceAreaCreateRequest serviceAreaCreateRequest) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceAreaRestMapper.toServiceAreaResponse(
                                                serviceAreaServicePort.save(
                                                                serviceAreaRestMapper.toServiceArea(
                                                                                serviceAreaCreateRequest))));
        }

        /**
         * Actualiza los datos de un área de servicio existente.
         *
         * Este endpoint recibe el ID del área de servicio a actualizar y los nuevos
         * datos en formato JSON.
         * Luego, transforma los datos a un modelo de dominio mediante el mapper
         * correspondiente,
         * y delega la actualización al servicio de dominio. Finalmente, devuelve la
         * información del área de servicio actualizada en formato de respuesta (DTO).
         *
         * @param serviceAreaId ID del área de servicio a actualizar
         * @param request       DTO de entrada con los nuevos datos del área de servicio
         * @return Información del área de servicio actualizada en formato de respuesta
         *         (DTO)
         */
        @Operation(summary = "Actualizar área de servicio", description = "Este endpoint permite actualizar los datos de un área de servicio existente. "
                        +
                        "Se requiere el ID del área de servicio a actualizar y los nuevos datos en formato JSON.")
        @PutMapping("/v1/api/{serviceAreaId}")
        public ResponseEntity<?> updateServiceArea(
                        @Parameter(description = "ID del área de servicio a actualizar", required = true) @PathVariable UUID serviceAreaId,
                        @Valid @RequestBody ServiceAreaCreateRequest request) {
                return ResponseEntity.ok(
                                serviceAreaRestMapper.toServiceAreaResponse(
                                                serviceAreaServicePort.update(
                                                                serviceAreaId,
                                                                serviceAreaRestMapper.toServiceArea(request))));
        }

        /**
         * Elimina un área de servicio existente del sistema.
         *
         * Este endpoint recibe el ID del área de servicio a eliminar, delega la
         * eliminación al servicio de dominio, y devuelve una respuesta sin contenido
         * (204 No Content) si la eliminación fue exitosa.
         * Si el área de servicio no existe, se maneja la excepción correspondiente en
         * el controlador de errores global.
         *
         * @param serviceAreaId ID del área de servicio a eliminar
         * @return ResponseEntity sin contenido (HTTP 204) si la eliminación es exitosa
         */
        @Operation(summary = "Eliminar área de servicio", description = "Este endpoint permite eliminar un área de servicio existente. "
                        +
                        "Se requiere el ID del área de servicio a eliminar.")
        @DeleteMapping("/v1/api/{serviceAreaId}")
        public ResponseEntity<Void> deleteServiceArea(
                        @Parameter(description = "ID del área de servicio a eliminar", required = true) @PathVariable UUID serviceAreaId) {
                serviceAreaServicePort.delete(serviceAreaId);
                return ResponseEntity.noContent().build();
        }
}
