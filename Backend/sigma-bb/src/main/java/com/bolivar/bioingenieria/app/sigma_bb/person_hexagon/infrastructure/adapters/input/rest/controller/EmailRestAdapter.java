package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.PersonServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.mapper.EmailPersonRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.mapper.PersonRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request.EmailPersonCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.response.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Adaptador REST para la gestión de emailPerson.
 *
 * Esta clase expone los endpoints HTTP relacionados con el recurso persona,
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
@RequestMapping("/person/email")
@Tag(name = "EmailPerson REST API", description = "Endpoints para la gestión de EmailPerson")
public class EmailRestAdapter {

        private final PersonServicePort personServicePort;

        private final PersonRestMapper personRestMapper;
        private final EmailPersonRestMapper emailPersonRestMapper;

        // -------------------------------------------------------
        // -------------------- CRUD EMAIL ---------------------
        // -------------------------------------------------------

        /**
         * Agrega un correo electrónico a una persona existente.
         *
         * Este endpoint recibe el identificador de la persona y los datos del correo,
         * los valida y los transforma a un modelo de dominio para asociarlo a la
         * persona.
         * Luego, retorna la persona actualizada en formato de respuesta.
         *
         * Retorna una respuesta con estado HTTP 200 (OK).
         *
         * @param id      Identificador único de la persona
         * @param request Datos del correo electrónico a agregar
         * @return ResponseEntity con la persona actualizada (DTO)
         */
        @Operation(summary = "Agregar correo electrónico a una persona", description = "Agrega un nuevo correo electrónico a una persona existente, identificada por su ID.")
        @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')")
        @PutMapping("/v1/api/{id}")
        public ResponseEntity<PersonResponse> createEmailPerson(
                        @Parameter(description = "Identificador único de la persona a la que se le agregará el correo electrónico (UUID)", required = true) @PathVariable UUID id,
                        @Valid @RequestBody EmailPersonCreateRequest request) {

                return ResponseEntity.ok(
                                personRestMapper.toPersonResponse(
                                                personServicePort.addEmail(id,
                                                                emailPersonRestMapper.toEmailPerson(request))));
        }

        /**
         * Actualiza un correo electrónico asociado a una persona.
         *
         * Este endpoint recibe el identificador de la persona, el identificador del
         * correo
         * y los nuevos datos, los valida y los transforma a un modelo de dominio para
         * realizar
         * la actualización. Luego, retorna la persona actualizada en formato de
         * respuesta.
         *
         * Retorna una respuesta con estado HTTP 200 (OK).
         *
         * @param id      Identificador único de la persona
         * @param emailId Identificador único del correo electrónico a actualizar
         * @param request Datos actualizados del correo electrónico
         * @return ResponseEntity con la persona actualizada (DTO)
         */
        @Operation(summary = "Actualizar correo electrónico de una persona", description = "Actualiza un correo electrónico asociado a una persona utilizando el ID de la persona, el ID del correo electrónico y los nuevos datos proporcionados.")
        @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')")
        @PutMapping("/v1/api/{id}/{emailId}")
        public ResponseEntity<PersonResponse> updateEmailPerson(
                        @Parameter(description = "Identificador único de la persona a actualizar (UUID)", required = true) @PathVariable UUID id,
                        @Parameter(description = "Identificador único del correo electrónico a actualizar (UUID)", required = true) @PathVariable UUID emailId,
                        @Valid @RequestBody EmailPersonCreateRequest request) {
                return ResponseEntity.ok(
                                personRestMapper.toPersonResponse(
                                                personServicePort.updateEmail(id, emailId,
                                                                emailPersonRestMapper.toEmailPerson(request))));
        }

        /**
         * Elimina un correo electrónico asociado a una persona.
         *
         * Este endpoint recibe el identificador de la persona y del correo,
         * y ejecuta la operación de eliminación en el servicio de dominio.
         * Luego, retorna la persona actualizada en formato de respuesta.
         *
         * Retorna una respuesta con estado HTTP 200 (OK).
         *
         * @param id      Identificador único de la persona
         * @param emailId Identificador único del correo electrónico a eliminar
         * @return ResponseEntity con la persona actualizada (DTO)
         */
        @Operation(summary = "Eliminar correo electrónico de una persona", description = "Elimina un correo electrónico asociado a una persona utilizando el ID de la persona y el ID del correo electrónico.")
        @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')")
        @DeleteMapping("/v1/api/{id}/{emailId}")
        public ResponseEntity<PersonResponse> deleteEmailPerson(
                        @Parameter(description = "Identificador único de la persona a la que pertenece el correo electrónico (UUID)", required = true) @PathVariable UUID id,
                        @Parameter(description = "Identificador único del correo electrónico a eliminar (UUID)", required = true) @PathVariable UUID emailId) {
                return ResponseEntity.ok(
                                personRestMapper.toPersonResponse(
                                                personServicePort.removeEmail(id, emailId)));
        }
}
