package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.HeadquarterServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.HeadquarterRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.headquarter_request.HeadquarterCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Adaptador REST para la gestión de sedes (Headquarter).
 *
 * Esta clase expone los endpoints HTTP relacionados con el recurso sede,
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
@RequestMapping("/headquarter")
@Tag(name = "Headquarter REST API",
        description = "Endpoints para la gestión de Headquarter")
public class HeadquarterRestAdapter {

    private final HeadquarterServicePort headquarterServicePort;

    private final HeadquarterRestMapper headquarterRestMapper;

    // -------------------------------------------------------
    // -------------------- CRUD SEDE ------------------------
    // -------------------------------------------------------

    /**
     * Obtiene la lista de todas las sedes registradas.
     *
     * Este endpoint consulta el servicio de dominio para recuperar
     * todas las sedes y las transforma a objetos de respuesta
     * mediante el mapper correspondiente.
     *
     * @return Lista de sedes en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Obtener todas las sedes",
            description = "Este endpoint devuelve una lista de todas las sedes registradas en el sistema.")
    @GetMapping("/v1/api")
    public List<?> getAllHeadquarters() {
        return headquarterRestMapper.toHeadquarterResponseList(headquarterServicePort.findAll());
    }

    /**
     * Obtiene la información de una sede específica por su ID.
     *
     * Este endpoint consulta el servicio de dominio para recuperar
     * la sede identificada por el ID proporcionado y la transforma
     * a un objeto de respuesta mediante el mapper correspondiente.
     *
     * @param headquarterId ID de la sede a consultar
     * @return Información de la sede en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Obtener una sede por ID",
            description = "Este endpoint devuelve la información de una sede específica identificada por su ID.")
    @GetMapping("/v1/api/{headquarterId}")
    public Object getHeadquarterById(
            @Parameter(
                    description = "ID de la sede a consultar",
                    required = true)
            @PathVariable UUID headquarterId) {
        return headquarterRestMapper.toHeadquarterResponse(headquarterServicePort.findByID(headquarterId));
    }

    /**
     * Crea una nueva sede en el sistema.
     *
     * Este endpoint recibe los datos de una nueva sede en formato JSON,
     * los valida y los transforma a una entidad de dominio para ser persistida.
     * Luego, convierte el resultado a un objeto de respuesta.
     *
     * @param headquarterCreateRequest DTO de entrada con los datos de la nueva sede
     * @return Información de la sede creada en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Crear una nueva sede",
            description = "Este endpoint permite crear una nueva sede en el sistema. " +
                    "Recibe los datos de la sede en formato JSON, los valida y los transforma a una entidad de dominio para ser persistida. " +
                    "Luego, convierte el resultado a un objeto de respuesta.")
    @PostMapping("/v1/api")
    public ResponseEntity<?> createHeadquarter(
            @RequestBody HeadquarterCreateRequest headquarterCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(headquarterRestMapper.toHeadquarterResponse(
                        headquarterServicePort.save(
                                headquarterRestMapper.toHeadquarter(headquarterCreateRequest))));
    }

    /**
     * Actualiza la información de una sede existente.
     *
     * Este endpoint recibe el ID de la sede a actualizar y los nuevos datos en formato JSON,
     * los valida y los transforma a una entidad de dominio para ser persistida. Luego, convierte
     * el resultado a un objeto de respuesta.
     *
     * @param headquarterId ID de la sede a actualizar
     * @param headquarterUpdateRequest DTO de entrada con los nuevos datos de la sede
     * @return Información de la sede actualizada en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Actualizar una sede existente",
            description = "Este endpoint permite actualizar la información de una sede existente. " +
                    "Recibe el ID de la sede a actualizar y los nuevos datos en formato JSON, los valida y los transforma a una entidad de dominio para ser persistida. " +
                    "Luego, convierte el resultado a un objeto de respuesta.")
    @PutMapping("/v1/api/{headquarterId}")
    public ResponseEntity<?> updateHeadquarter(
            @Parameter(
                    description = "ID de la sede a actualizar",
                    required = true)
            @PathVariable UUID headquarterId,
            @RequestBody HeadquarterCreateRequest headquarterUpdateRequest) {
        return ResponseEntity.ok(
                headquarterRestMapper.toHeadquarterResponse(
                        headquarterServicePort.update(
                                headquarterId,
                                headquarterRestMapper.toHeadquarter(headquarterUpdateRequest))));

    }

    /**
     * Elimina una sede del sistema.
     *
     * Este endpoint recibe el ID de la sede a eliminar y ejecuta la operación de eliminación
     * en el servicio de dominio. Retorna una respuesta con estado HTTP 204 (NO CONTENT) si la eliminación es exitosa.
     *
     * @param headquarterId ID de la sede a eliminar
     * @return ResponseEntity sin contenido (HTTP 204) si la eliminación es exitosa
     */
    @Operation(
            summary = "Eliminar una sede",
            description = "Este endpoint permite eliminar una sede del sistema. " +
                    "Recibe el ID de la sede a eliminar y ejecuta la operación de eliminación.")
    @DeleteMapping("/v1/api/{headquarterId}")
    public ResponseEntity<Void> deleteHeadquarter(
            @Parameter(
                    description = "ID de la sede a eliminar",
                    required = true)
            @PathVariable UUID headquarterId) {
        headquarterServicePort.delete(headquarterId);
        return ResponseEntity.noContent().build();
    }
}
