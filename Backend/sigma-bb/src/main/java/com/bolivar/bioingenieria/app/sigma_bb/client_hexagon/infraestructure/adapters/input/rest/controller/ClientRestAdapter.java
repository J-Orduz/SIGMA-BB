package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.input.ClientServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper.ClientRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request.ClientCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador REST para la gestión de clientes.
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
@RequestMapping("/client")
@Tag(name = "Client REST API", description = "Endpoints para la gestión de Client")
public class ClientRestAdapter {

        private final ClientServicePort clientServicePort;

        private final ClientRestMapper clientRestMapper;

        // -------------------------------------------------------
        // -------------------- CRUD CLIENTE ---------------------
        // -------------------------------------------------------

        /**
         * Obtiene la lista de todos los clientes registrados.
         *
         * Este endpoint consulta el servicio de dominio para recuperar
         * todos los clientes y las transforma a objetos de respuesta
         * mediante el mapper correspondiente.
         *
         * @return Lista de clientes en formato de respuesta (DTO)
         */
        @Operation(summary = "Obtener todos los clientes", description = "Este endpoint devuelve una lista de todos los clientes registrados en el sistema.")
        @GetMapping("/v1/api")
        public List<?> getAllClients() {
                return clientRestMapper.toClientResponseList(clientServicePort.findAll());
        }

        /**
         * Obtiene los detalles de un cliente específico por su ID.
         *
         * Este endpoint consulta el servicio de dominio para recuperar
         * la información de un cliente identificado por su ID y la
         * transforma a un objeto de respuesta mediante el mapper correspondiente.
         *
         * @param clientId Identificador único del cliente a consultar
         * @return Detalles del cliente en formato de respuesta (DTO)
         */
        @Operation(summary = "Obtener cliente por ID", description = "Este endpoint devuelve los detalles de un cliente específico identificado por su ID.")
        @GetMapping("/v1/api/{clientId}")
        public Object getClientById(
                        @Parameter(description = "Identificador único del cliente", required = true) @PathVariable String clientId) {
                return clientRestMapper.toClientResponse(clientServicePort.findByID(clientId));
        }

        /**
         * Crea un nuevo cliente en el sistema.
         *
         * Este endpoint recibe los datos de un nuevo cliente en formato JSON,
         * los valida y los transforma a una entidad de dominio para ser persistida.
         * Luego, convierte el resultado a un objeto de respuesta.
         *
         * @param clientCreateRequest Objeto de solicitud con los datos del cliente a
         *                            crear
         * @return Detalles del cliente creado en formato de respuesta (DTO)
         */
        @Operation(summary = "Crear nuevo cliente", description = "Este endpoint permite crear un nuevo cliente en el sistema. "
                        +
                        "Se requiere enviar un objeto JSON con los datos del cliente a crear.")
        @PostMapping("/v1/api")
        public ResponseEntity<?> createClient(
                        @Valid @RequestBody ClientCreateRequest clientCreateRequest) {

                System.out.println("\n\n\n\n\n" + clientCreateRequest + "\n\n\n\n\n");
                System.out.println("\n\n\n\n\n" + clientRestMapper.toClient(clientCreateRequest) + "\n\n\n\n");

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(clientRestMapper.toClientResponse(
                                                clientServicePort
                                                                .save(clientRestMapper.toClient(clientCreateRequest))));
        }

        /**
         * Actualiza la información de un cliente específico por su ID.
         *
         * Este endpoint recibe los datos actualizados de un cliente en formato JSON,
         * los valida y los transforma a una entidad de dominio para ser persistida.
         * Luego, convierte el resultado a un objeto de respuesta.
         *
         * @param clientId Identificador único del cliente a actualizar
         * @param request  Objeto de solicitud con los datos actualizados del cliente
         * @return Detalles del cliente actualizado en formato de respuesta (DTO)
         */
        @Operation(summary = "Actualizar cliente por ID", description = "Este endpoint permite actualizar la información de un cliente específico identificado por su ID. "
                        +
                        "Se requiere enviar un objeto JSON con los datos actualizados del cliente.")
        @PutMapping("/v1/api/{clientId}")
        public ResponseEntity<?> updateClient(
                        @Parameter(description = "Identificador único del cliente a actualizar", required = true) @PathVariable String clientId,
                        @Valid @RequestBody ClientCreateRequest request) {
                return ResponseEntity.ok(
                                clientRestMapper.toClientResponse(
                                                clientServicePort.update(clientId,
                                                                clientRestMapper.toClient(request))));
        }

        /**
         * Elimina un cliente específico por su ID.
         *
         * Este endpoint permite eliminar un cliente identificado por su ID.
         * Si el cliente existe, se elimina del sistema y se retorna una respuesta
         * con estado HTTP 204 (NO CONTENT). Si el cliente no existe, se maneja
         * la excepción correspondiente en el controlador de errores global.
         *
         * @param clientId Identificador único del cliente a eliminar
         * @return ResponseEntity sin contenido (HTTP 204) si la eliminación es exitosa
         */
        @Operation(summary = "Eliminar cliente por ID", description = "Este endpoint permite eliminar un cliente específico identificado por su ID.")
        @DeleteMapping("/v1/api/{clientId}")
        public ResponseEntity<Void> deleteClient(
                        @Parameter(description = "Identificador único del cliente a eliminar", required = true) @PathVariable String clientId) {
                clientServicePort.delete(clientId);
                return ResponseEntity.noContent().build();
        }
}
