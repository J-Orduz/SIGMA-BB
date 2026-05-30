package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.PersonServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.mapper.PersonRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.mapper.PhonePersonRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request.PhonePersonCreateRequest;
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
 * Adaptador REST para la gestión de phonePerson.
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
@RequestMapping("/person/phone")
@Tag(name = "PhonePerson REST API", description = "Endpoints para la gestión de PhonePerson")
public class PhoneRestAdapter {

        private final PersonServicePort personServicePort;

        private final PersonRestMapper personRestMapper;
        private final PhonePersonRestMapper phonePersonRestMapper;

        // -------------------------------------------------------
        // -------------------- CRUD TELEFONO ---------------------
        // -------------------------------------------------------

        /**
         * Agrega un teléfono a una persona existente.
         *
         * Este endpoint recibe el identificador de la persona y los datos del teléfono,
         * los valida y los transforma a un modelo de dominio para asociarlo a la
         * persona.
         * Luego, retorna la persona actualizada en formato de respuesta.
         *
         * Retorna una respuesta con estado HTTP 200 (OK).
         *
         * @param id      Identificador único de la persona
         * @param request Datos del teléfono a agregar
         * @return ResponseEntity con la persona actualizada (DTO)
         */
        @Operation(summary = "Agregar teléfono a persona", description = "Agrega un nuevo teléfono a una persona existente, actualizando su información.")

        @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')") // Solo usuarios con permiso 'admin.full' pueden
                                                                       // agregar teléfonos
        @PutMapping("/v1/api/{id}")
        public ResponseEntity<PersonResponse> createPhonePerson(
                        @Parameter(description = "Identificador único de la persona a la que se le agregará el teléfono (UUID)", required = true) @PathVariable UUID id,
                        @Valid @RequestBody PhonePersonCreateRequest request) {

                return ResponseEntity.ok(
                                personRestMapper.toPersonResponse(
                                                personServicePort.addPhone(id,
                                                                phonePersonRestMapper.toPhonePerson(request))));
        }

        /**
         * Actualiza un teléfono asociado a una persona.
         *
         * Este endpoint recibe el identificador de la persona, el identificador del
         * teléfono
         * y los nuevos datos, los valida y los transforma a un modelo de dominio para
         * realizar
         * la actualización. Luego, retorna la persona actualizada en formato de
         * respuesta.
         *
         * Retorna una respuesta con estado HTTP 200 (OK).
         *
         * @param id      Identificador único de la persona
         * @param phoneId Identificador único del teléfono a actualizar
         * @param request Datos actualizados del teléfono
         * @return ResponseEntity con la persona actualizada (DTO)
         */
        @Operation(summary = "Actualizar teléfono de persona", description = "Actualiza un teléfono asociado a una persona utilizando el ID de la persona, el ID del teléfono y los nuevos datos proporcionados.")
        @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')") //
        @PutMapping("/v1/api/{id}/{phoneId}")
        public ResponseEntity<PersonResponse> updatePhonePerson(
                        @Parameter(description = "Identificador único de la persona a actualizar (UUID)", required = true) @PathVariable UUID id,
                        @Parameter(description = "Identificador único del teléfono a actualizar (UUID)", required = true) @PathVariable UUID phoneId,
                        @Valid @RequestBody PhonePersonCreateRequest request) {
                return ResponseEntity.ok(
                                personRestMapper.toPersonResponse(
                                                personServicePort.updatePhone(id, phoneId,
                                                                phonePersonRestMapper.toPhonePerson(request))));
        }

        /**
         * Elimina un teléfono asociado a una persona.
         *
         * Este endpoint recibe el identificador de la persona y del teléfono,
         * y ejecuta la operación de eliminación en el servicio de dominio.
         * Luego, retorna la persona actualizada en formato de respuesta.
         *
         * Retorna una respuesta con estado HTTP 200 (OK).
         *
         * @param id      Identificador único de la persona
         * @param phoneId Identificador único del teléfono a eliminar
         * @return ResponseEntity con la persona actualizada (DTO)
         */
        @Operation(summary = "Eliminar teléfono de persona", description = "Elimina un teléfono asociado a una persona utilizando el ID de la persona y el ID del teléfono.")
        @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')") // Solo usuarios con permiso 'admin.full' pueden
                                                                       // eliminar teléfonos
        @DeleteMapping("/v1/api/{id}/{phoneId}")
        public ResponseEntity<PersonResponse> deletePhonePerson(
                        @Parameter(description = "Identificador único de la persona a actualizar (UUID)", required = true) @PathVariable UUID id,
                        @Parameter(description = "Identificador único del teléfono a eliminar (UUID)", required = true) @PathVariable UUID phoneId) {

                return ResponseEntity.ok(
                                personRestMapper.toPersonResponse(
                                                personServicePort.removePhone(id, phoneId)));
        }
}
