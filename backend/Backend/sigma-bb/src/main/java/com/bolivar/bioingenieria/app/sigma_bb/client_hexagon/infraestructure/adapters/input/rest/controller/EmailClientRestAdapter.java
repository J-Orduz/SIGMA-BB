package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ClientServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ClientRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.EmailClientRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request.EmailClientCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.client_responce.ClientResponse;
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
 * Adaptador REST para la gestión de emailClient.
 *
 * Esta clase expone los endpoints HTTP relacionados con el recurso cliente,
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
@RequestMapping("/client/email")
@Tag(
        name = "EmailClient REST API",
        description = "Endpoints para la gestión de EmailClient")
public class EmailClientRestAdapter {

    private final ClientServicePort clientServicePort;

    private final ClientRestMapper clientRestMapper;
    private final EmailClientRestMapper emailClientRestMapper;

     // --------------------------------------------------------
     // -------------------- CRUD CORREO -----------------------
     // --------------------------------------------------------

    /**
     * Agrega un correo electrónico a un cliente existente.
     *
     * Este endpoint recibe el identificador del cliente y los datos del correo electrónico,
     * los valida y los transforma a un modelo de dominio para asociarlo al cliente.
     * Luego, retorna el cliente actualizado en formato de respuesta.
     *
     * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto con la información actualizada del cliente.
     *
     * @param idClient Identificador único del cliente al que se le va a agregar el correo electrónico.
     * @param request Datos del correo electrónico a agregar.
     * @return ResponseEntity con el cliente actualizado y estado HTTP 200 (OK).
     */
    @Operation(
            summary = "Agregar correo electrónico a un cliente",
            description = "Este endpoint permite agregar una dirección de correo electrónico a un cliente existente en el sistema. " +
                    "Se requiere el identificador único del cliente y los datos del correo electrónico a agregar. " +
                    "Retorna la información actualizada del cliente con el nuevo correo asociado.")
    @PutMapping("/v1/api/{idClient}")
    public ResponseEntity<ClientResponse> createEmailClient(
            @Parameter(
                    description = "Identificador único del cliente al que se le va a agregar el correo electrónico",
                    required = true)
            @PathVariable("idClient") String idClient,
            @Valid @RequestBody EmailClientCreateRequest request) {
        return ResponseEntity.ok(
                clientRestMapper.toClientResponse(
                        clientServicePort.addEmail(
                                idClient,
                                emailClientRestMapper.toEmailClient(request))));
    }

    /**
     * Actualiza un correo electrónico asociado a un cliente existente.
     *
     * Este endpoint recibe el identificador del cliente, el identificador del correo electrónico a actualizar y los nuevos datos del correo electrónico,
     * los valida y los transforma a un modelo de dominio para actualizar la asociación al cliente.
     * Luego, retorna el cliente actualizado en formato de respuesta.
     *
     * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto con la información actualizada del cliente.
     *
     * @param idClient Identificador único del cliente al que se le va a actualizar el correo electrónico.
     * @param emailId Identificador único del correo electrónico a actualizar.
     * @param request Datos actualizados del correo electrónico.
     * @return ResponseEntity con el cliente actualizado y estado HTTP 200 (OK).
     */
    @Operation(
            summary = "Actualizar correo electrónico de un cliente",
            description = "Este endpoint permite actualizar una dirección de correo electrónico asociada a un cliente existente en el sistema. " +
                    "Se requiere el identificador único del cliente, el identificador del correo electrónico a actualizar y los nuevos datos del correo. " +
                    "Retorna la información actualizada del cliente con el correo modificado.")
    @PutMapping("/v1/api/{idClient}/{emailId}")
    public ResponseEntity<ClientResponse> updateEmailClient(
            @Parameter(
                    description = "Identificador único del cliente al que se le va a actualizar el correo electrónico",
                    required = true)
            @PathVariable("idClient") String idClient,
            @Parameter(
                    description = "Identificador único del correo electrónico a actualizar",
                    required = true)
            @PathVariable("emailId") UUID emailId,
            @Valid @RequestBody EmailClientCreateRequest request) {
        return ResponseEntity.ok(
                clientRestMapper.toClientResponse(
                        clientServicePort.updateEmail(
                                idClient,
                                emailId,
                                emailClientRestMapper.toEmailClient(request))));
    }

    /**
     * Elimina un correo electrónico asociado a un cliente existente.
     *
     * Este endpoint recibe el identificador del cliente y el identificador del correo electrónico a eliminar,
     * los valida y realiza la eliminación de la asociación del correo al cliente.
     * Luego, retorna el cliente actualizado en formato de respuesta sin el correo eliminado.
     *
     * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto con la información actualizada del cliente sin el correo eliminado.
     *
     * @param idClient Identificador único del cliente al que se le va a eliminar el correo electrónico.
     * @param emailId Identificador único del correo electrónico a eliminar.
     * @return ResponseEntity con el cliente actualizado sin el correo eliminado y estado HTTP 200 (OK).
     */
    @Operation(
            summary = "Eliminar correo electrónico de un cliente",
            description = "Este endpoint permite eliminar una dirección de correo electrónico asociada a un cliente existente en el sistema. " +
                    "Se requiere el identificador único del cliente y el identificador del correo electrónico a eliminar. " +
                    "Retorna la información actualizada del cliente sin el correo eliminado.")
    @DeleteMapping("/v1/api/{idClient}/{emailId}")
    public ResponseEntity<ClientResponse> deleteEmailClient(
            @Parameter(
                    description = "Identificador único del cliente al que se le va a eliminar el correo electrónico",
                    required = true)
            @PathVariable String idClient,
            @Parameter(
                    description = "Identificador único del correo electrónico a eliminar",
                    required = true)
            @PathVariable UUID emailId) {
        return ResponseEntity.ok(
                clientRestMapper.toClientResponse(
                        clientServicePort.removeEmail(idClient, emailId)));
    }
}
