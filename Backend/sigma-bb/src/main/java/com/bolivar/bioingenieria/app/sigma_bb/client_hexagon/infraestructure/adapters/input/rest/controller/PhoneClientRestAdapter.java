package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ClientServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ClientRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.PhoneClientRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request.PhoneClientCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.client_responce.ClientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Adaptador REST para la gestión de phoneClient.
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
@RequestMapping("/client/phone")
@Tag(name = "PhoneClient REST API", description = "Endpoints para la gestión de PhoneClient")
public class PhoneClientRestAdapter {

        private final ClientServicePort clientServicePort;

        private final ClientRestMapper clientRestMapper;
        private final PhoneClientRestMapper phoneClientRestMapper;

        // --------------------------------------------------------
        // -------------------- CRUD TELEFONO ---------------------
        // --------------------------------------------------------

        /**
         * Agrega un teléfono a un cliente existente.
         *
         * Este endpoint recibe el identificador del cliente y los datos del teléfono,
         * los valida y los transforma a un modelo de dominio para asociarlo al cliente.
         * Luego, retorna el cliente actualizado en formato de respuesta.
         *
         * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto
         * con la información actualizada del cliente.
         *
         * @param idClient Identificador único del cliente al que se le va a agregar el
         *                 teléfono.
         * @param request  Datos del teléfono a agregar.
         * @return ResponseEntity con el cliente actualizado y estado HTTP 200 (OK).
         */
        @Operation(summary = "Agregar teléfono a cliente", description = "Agrega un nuevo número telefónico a un cliente existente en el sistema. "
                        +
                        "El endpoint recibe el identificador del cliente y los datos del teléfono, " +
                        "valida la información y asocia el nuevo número al cliente. " +
                        "Retorna la información actualizada del cliente con el nuevo teléfono asociado.")
        @PutMapping("/v1/api/{idClient}")
        public ResponseEntity<ClientResponse> createPhoneClient(
                        @Parameter(description = "Identificador único del cliente al que se le va a agregar el teléfono", required = true) @PathVariable String idClient,
                        @Valid @RequestBody PhoneClientCreateRequest request) {

                return ResponseEntity.ok(
                                clientRestMapper.toClientResponse(
                                                clientServicePort.addPhone(idClient,
                                                                phoneClientRestMapper.toPhoneClient(request))));
        }

        /**
         * Actualiza un teléfono asociado a un cliente existente.
         *
         * Este endpoint recibe el identificador del cliente, el identificador del
         * teléfono a actualizar y los nuevos datos del teléfono,
         * los valida y los transforma a un modelo de dominio para actualizar la
         * asociación al cliente.
         * Luego, retorna el cliente actualizado en formato de respuesta.
         *
         * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto
         * con la información actualizada del cliente.
         *
         * @param clienId Identificador único del cliente al que se le va a actualizar
         *                el teléfono.
         * @param phoneId Identificador único del teléfono a actualizar.
         * @param request Datos actualizados del teléfono.
         * @return ResponseEntity con el cliente actualizado y estado HTTP 200 (OK).
         */
        @Operation(summary = "Actualizar teléfono de cliente", description = "Actualiza un número telefónico asociado a un cliente existente en el sistema. "
                        +
                        "El endpoint recibe el identificador del cliente, el identificador del teléfono a actualizar y los nuevos datos del teléfono, "
                        +
                        "valida la información y actualiza el número asociado al cliente. " +
                        "Retorna la información actualizada del cliente con el teléfono modificado.")
        @PutMapping("/v1/api/{clienId}/{phoneId}")
        public ResponseEntity<ClientResponse> updatePhoneClient(
                        @Parameter(description = "Identificador único del cliente al que se le va a actualizar el teléfono", required = true) @PathVariable String clienId,
                        @Parameter(description = "Identificador único del teléfono a actualizar", required = true) @PathVariable UUID phoneId,
                        @Valid @RequestBody PhoneClientCreateRequest request) {

                return ResponseEntity.ok(
                                clientRestMapper.toClientResponse(
                                                clientServicePort.updatePhone(clienId, phoneId,
                                                                phoneClientRestMapper.toPhoneClient(request))));
        }

        /**
         * Elimina un teléfono asociado a un cliente existente.
         *
         * Este endpoint recibe el identificador del cliente y el identificador del
         * teléfono a eliminar,
         * valida la información y elimina la asociación del número al cliente.
         * Luego, retorna el cliente actualizado en formato de respuesta sin el teléfono
         * eliminado.
         *
         * Retorna un código de estado HTTP 200 (OK) si la operación fue exitosa, junto
         * con la información actualizada del cliente sin el teléfono eliminado.
         *
         * @param clientId Identificador único del cliente al que se le va a eliminar el
         *                 teléfono.
         * @param phoneId  Identificador único del teléfono a eliminar.
         * @return ResponseEntity con el cliente actualizado sin el teléfono eliminado y
         *         estado HTTP 200 (OK).
         */
        @Operation(summary = "Eliminar teléfono de cliente", description = "Elimina un número telefónico asociado a un cliente existente en el sistema. "
                        +
                        "El endpoint recibe el identificador del cliente y el identificador del teléfono a eliminar, " +
                        "valida la información y elimina la asociación del número al cliente. " +
                        "Retorna la información actualizada del cliente sin el teléfono eliminado.")
        @DeleteMapping("/v1/api/{clientId}/{phoneId}")
        public ResponseEntity<ClientResponse> deletePhoneClient(
                        @Parameter(description = "Identificador único del cliente al que se le va a eliminar el teléfono", required = true) @PathVariable String clientId,
                        @Parameter(description = "Identificador único del teléfono a eliminar", required = true) @PathVariable UUID phoneId) {

                return ResponseEntity.ok(
                                clientRestMapper.toClientResponse(
                                                clientServicePort.removePhone(clientId, phoneId)));
        }

}