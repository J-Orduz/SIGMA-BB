package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ClientEquipmentServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ClientServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ClientEquipmentRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.clientEqupment_request.ClientEquipmentCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Adaptador REST para la gestión de Client Equipment.
 *
 * Esta clase expone los endpoints HTTP relacionados con el recurso Client Equipment,
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
@RequestMapping("/client-equipment")
@Tag(
        name = "Client Equipment REST API",
        description = "Endpoints para la gestión de Client Equipment")
public class ClientEquipmentRestAdapter {

    private final ClientEquipmentServicePort clientEquipmentServicePort;

    private final ClientEquipmentRestMapper clientEquipmentRestMapper;

    // --------------------------------------------------------------
    // -------------------- CRUD EQUIPO CLIENTE ---------------------
    // --------------------------------------------------------------

    /**
     * Obtiene la lista de todos los equipos asociados a clientes registrados.
     * <p>
     * Este endpoint consulta el servicio de dominio para recuperar
     * todos los equipos vinculados a clientes y las transforma a objetos
     * de respuesta (DTO) mediante el mapper correspondiente.
     *
     * @return Lista de equipos de clientes en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Obtener todos los equipos de clientes",
            description = "Este endpoint devuelve una lista de todos los equipos asociados a clientes registrados en el sistema.")
    @GetMapping("/v1/api")
    public List<?> getAllClientEquipments() {
        return clientEquipmentRestMapper.toClientEquipmentResponseList(clientEquipmentServicePort.findAll());
    }

    /**
     * Obtiene la información de un equipo de cliente específico por su ID.
     * <p>
     * Este endpoint consulta el servicio de dominio para recuperar
     * los detalles de un equipo asociado a un cliente, identificado
     * por su ID único, y lo transforma a un objeto de respuesta (DTO)
     * mediante el mapper correspondiente.
     *
     * @param clientEquipmentId ID del equipo del cliente a consultar
     * @return Detalles del equipo del cliente en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Obtener equipo de cliente por ID",
            description = "Este endpoint devuelve la información de un equipo asociado a un cliente específico, identificado por su ID único."
    )
    @GetMapping("/v1/api/{clientEquipmentId}")
    public Object getClientEquipmentById(
            @Parameter(
                    description = "ID del equipo del cliente a consultar",
                    required = true)
            @PathVariable UUID clientEquipmentId) {
        return clientEquipmentRestMapper.toClientEquipmentResponse(clientEquipmentServicePort.findByID(clientEquipmentId));
    }

    /**
     * Crea un nuevo equipo asociado a un cliente en el sistema.
     * <p>
     * Este endpoint recibe los datos de un nuevo equipo de cliente en formato JSON,
     * los transforma a un modelo de dominio mediante el mapper correspondiente,
     * y luego invoca el servicio de dominio para guardar el nuevo equipo. Finalmente,
     * devuelve la información del equipo creado en formato de respuesta (DTO).
     *
     * @param clientEquipmentCreateRequest DTO de entrada con los datos del nuevo equipo de cliente
     * @return Información del equipo de cliente creado en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Crear nuevo equipo de cliente",
            description = "Este endpoint permite crear un nuevo equipo asociado a un cliente. Recibe los datos del equipo en formato JSON y devuelve la información del equipo creado."
    )
    @PostMapping("/v1/api")
    public ResponseEntity<?> createClientEquipment(
            @Valid @RequestBody ClientEquipmentCreateRequest clientEquipmentCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientEquipmentRestMapper.toClientEquipmentResponse(
                        clientEquipmentServicePort.save(
                                clientEquipmentRestMapper.toClientEquipment(clientEquipmentCreateRequest))));
    }

    /**
     * Actualiza la información de un equipo de cliente existente.
     * <p>
     * Este endpoint recibe el ID del equipo de cliente a actualizar y los nuevos datos en formato JSON.
     * Luego, transforma los datos a un modelo de dominio, invoca el servicio de dominio para realizar la actualización,
     * y finalmente devuelve la información del equipo actualizado en formato de respuesta (DTO).
     *
     * @param clientEquipmentId            ID del equipo del cliente a actualizar
     * @param clientEquipmentUpdateRequest DTO de entrada con los nuevos datos del equipo de cliente
     * @return Información del equipo de cliente actualizado en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Actualizar equipo de cliente",
            description = "Este endpoint permite actualizar la información de un equipo asociado a un cliente existente. Se requiere el ID del equipo a actualizar y los nuevos datos en formato JSON."
    )
    @PutMapping("/v1/api/{clientEquipmentId}")
    public ResponseEntity<?> updateClientEquipment(
            @Parameter(
                    description = "ID del equipo del cliente a actualizar",
                    required = true)
            @PathVariable UUID clientEquipmentId,
            @Valid @RequestBody ClientEquipmentCreateRequest clientEquipmentUpdateRequest) {
        return ResponseEntity.ok(
                    clientEquipmentRestMapper.toClientEquipmentResponse(
                            clientEquipmentServicePort.update(clientEquipmentId, clientEquipmentRestMapper.toClientEquipment(clientEquipmentUpdateRequest))));

    }

    /**
    * Elimina un equipo asociado a un cliente del sistema.
    * <p>
    * Este endpoint recibe el ID del equipo de cliente a eliminar, invoca el servicio de dominio para realizar la eliminación,
    * y devuelve una respuesta sin contenido (204 No Content) si la operación fue exitosa.
    *
    * @param clientEquipmentId ID del equipo del cliente a eliminar
    * @return Respuesta sin contenido (204 No Content) si la eliminación fue exitosa
    */
    @Operation(
            summary = "Eliminar equipo de cliente",
            description = "Este endpoint permite eliminar un equipo asociado a un cliente. Se requiere el ID del equipo a eliminar."
    )
    @DeleteMapping("/v1/api/{clientEquipmentId}")
    public ResponseEntity<Void> deleteClientEquipment(
            @Parameter(
                    description = "ID del equipo del cliente a eliminar",
                    required = true)
            @PathVariable UUID clientEquipmentId) {
        clientEquipmentServicePort.delete(clientEquipmentId);
        return ResponseEntity.noContent().build();
    }
}