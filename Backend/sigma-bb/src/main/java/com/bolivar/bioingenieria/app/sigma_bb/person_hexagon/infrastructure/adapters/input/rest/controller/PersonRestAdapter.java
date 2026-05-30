package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.PersonServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.mapper.PersonRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request.PersonCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request.PersonUpdateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.response.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Adaptador REST para la gestión de personas.
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
@RequestMapping("/person")
@Tag(name = "Person REST API",
        description = "Endpoints para la gestión de Person")
public class PersonRestAdapter {

    private final PersonServicePort personServicePort;

    private final PersonRestMapper personRestMapper;

    // -------------------------------------------------------
    // -------------------- CRUD PERSONA ---------------------
    // -------------------------------------------------------

    /**
     * Obtiene la lista de todas las personas registradas.
     *
     * Este endpoint consulta el servicio de dominio para recuperar
     * todas las personas y las transforma a objetos de respuesta
     * mediante el mapper correspondiente.
     *
     * @return Lista de personas en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Obtener todas las personas",
            description = "Recupera la lista completa de personas registradas en el sistema.")
    @PreAuthorize("hasAnyAuthority('admin.full', 'client.read', 'client.write')") // Solo los usuarios con autoridad 'admin.full' pueden acceder a esta información
    @GetMapping("/v1/api")
    public List<?> getAllPersons() {
        return personRestMapper.toPersonResponseList(personServicePort.findAll());
    }

    /**
     * Obtiene una persona específica a partir de su identificador único.
     *
     * Este endpoint consulta el servicio de dominio para buscar la persona
     * asociada al UUID proporcionado y la transforma al formato de respuesta.
     *
     * @param id Identificador único de la Person a consultar
     * @return Objeto de respuesta con la información de la persona encontrada
     */
    @Operation(
            summary = "Obtener persona por ID",
            description = "Recupera la información de una persona específica utilizando su identificador único (UUID).")
    @PreAuthorize("hasAnyAuthority('admin.full', 'client.read', 'client.write')") // Solo los usuarios con autoridad 'admin.full' pueden acceder a esta información
    @GetMapping("/v1/api/{id}")
    public Object getPersonById(
            @Parameter(description = "Identificador único de la persona (UUID)", required = true)
            @PathVariable UUID id) {
        return personRestMapper.toPersonResponse(personServicePort.findById(id));
    }

    /**
     * Crea una nueva persona en el sistema.
     *
     * Este endpoint recibe los datos de la persona, los valida y los transforma
     * a una entidad de dominio para ser persistida. Luego, convierte el resultado
     * a un objeto de respuesta.
     *
     * Retorna una respuesta con estado HTTP 201 (CREATED).
     *
     * @param request Datos de la persona a crear
     * @return ResponseEntity con la persona creada en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Crear nueva persona",
            description = "Crea una nueva persona en el sistema a partir de los datos proporcionados.")
    @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')") // Solo los usuarios con autoridad 'admin.full' pueden crear personas
    @PostMapping("/v1/api")
    public ResponseEntity<?> createPerson(
            @Valid @RequestBody PersonCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personRestMapper.toPersonResponse(
                        personServicePort.save(personRestMapper.toPerson(request))));
    }

    /**
     * Registra un nuevo ingeniero en el sistema.
     *
     * Este endpoint recibe los datos de la persona con rol de ingeniero, los valida
     * y los transforma a una entidad de dominio para ser persistida. Luego, convierte
     * el resultado a un objeto de respuesta.
     *
     * Retorna una respuesta con estado HTTP 201 (CREATED).
     *
     * @param request Datos del ingeniero a registrar
     * @return ResponseEntity con el ingeniero registrado en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Registrar nuevo ingeniero",
            description = "Registra una nueva persona con rol de ingeniero en el sistema a partir de los datos proporcionados.")
    @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')")// Solo los usuarios con autoridad 'admin.full' pueden registrar ingenieros
    @PostMapping("/vi/api/register/engineer")
    public ResponseEntity<?> registerEngineer(
            @Valid @RequestBody PersonCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personRestMapper.toPersonResponse(
                        personServicePort.registerEngineer(personRestMapper.toPersonCreateRequestUseCase(request))));
    }

    /**
     * Registra un nuevo administrador en el sistema.
     *
     * Este endpoint recibe los datos de la persona con rol de administrador, los valida
     * y los transforma a una entidad de dominio para ser persistida. Luego, convierte
     * el resultado a un objeto de respuesta.
     *
     * Retorna una respuesta con estado HTTP 201 (CREATED).
     *
     * @param request Datos del administrador a registrar
     * @return ResponseEntity con el administrador registrado en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Registrar nuevo administrador",
            description = "Registra una nueva persona con rol de administrador en el sistema a partir de los datos proporcionados."
    )
    @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')")// Solo los usuarios con autoridad 'admin.full' pueden registrar administradores
    @PostMapping("/v1/api/register/admin")
    public ResponseEntity<?> registerAdimn(
            @Valid @RequestBody PersonCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personRestMapper.toPersonResponse(
                        personServicePort.registerAdmin(personRestMapper.toPersonCreateRequestUseCase(request))));
    }

    /**
     * Registra un nuevo CEO-Client en el sistema.
     *
     * Este endpoint recibe los datos de la persona con rol de CEO-Client, los valida
     * y los transforma a una entidad de dominio para ser persistida. Luego, convierte
     * el resultado a un objeto de respuesta.
     *
     * Retorna una respuesta con estado HTTP 201 (CREATED).
     *
     * @param request Datos del CEO-Client a registrar
     * @return ResponseEntity con el CEO-Client registrado en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Registrar nuevo CEO-Client",
            description = "Registra una nueva persona con rol de CEO-Client en el sistema a partir de los datos proporcionados."
    )
    @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')") // Solo los usuarios con autoridad 'admin.full' pueden registrar CEO-Client
    @PostMapping("/v2/api/register/ceo-client")
    public ResponseEntity<?> registerCEOClient(
            @Valid @RequestBody PersonCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personRestMapper.toPersonResponse(
                        personServicePort.registerCEOClient(personRestMapper.toPersonCreateRequestUseCase(request))));
    }

    /**
     * Actualiza la información de una persona existente.
     *
     * Este endpoint recibe el identificador de la persona y los nuevos datos,
     * los valida y los transforma a un modelo de dominio para realizar la actualización.
     * Luego, convierte el resultado a un objeto de respuesta.
     *
     * Retorna una respuesta con estado HTTP 200 (OK).
     *
     * @param id Identificador único de la persona a actualizar
     * @param request Datos actualizados de la persona
     * @return ResponseEntity con la persona actualizada en formato de respuesta (DTO)
     */
    @Operation(
            summary = "Actualizar persona",
            description = "Actualiza la información de una persona existente utilizando su identificador único (UUID) y los nuevos datos proporcionados.")
    @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')") // Solo los usuarios con autoridad 'admin.full' pueden actualizar personas
    @PutMapping("/v1/api/{id}")
    public ResponseEntity<PersonResponse> updatePerson(
            @Parameter(
                    description = "Identificador único de la persona a actualizar (UUID)",
                    required = true)
            @PathVariable UUID id,
            @Valid @RequestBody PersonUpdateRequest request) {
        return ResponseEntity.ok(
                personRestMapper.toPersonResponse(
                        personServicePort.update(id, personRestMapper.tuUpdatePerson(request))));
    }

    /**
     * Elimina una persona del sistema.
     *
     * Este endpoint recibe el identificador de la persona y ejecuta
     * la operación de eliminación (Cambia el atributo b_estado_activo a false) en el servicio de dominio.
     *
     * Retorna una respuesta con estado HTTP 204 (NO_CONTENT)
     * indicando que la operación fue exitosa sin contenido en la respuesta.
     *
     * @param id Identificador único de la persona a eliminar
     * @return ResponseEntity sin contenido
     */
    @Operation(
            summary = "Eliminar persona",
            description = "Elimina una persona del sistema utilizando su identificador único (UUID).")
    @PreAuthorize("hasAnyAuthority('admin.full', 'client.write')")// Solo los usuarios con autoridad 'admin.full' pueden eliminar personas
    @DeleteMapping("/v1/api/{id}")
    public ResponseEntity<Void> deletePerson(
            @Parameter(
                    description = "Identificador único de la persona a eliminar (UUID)",
                    required = true)
            @PathVariable UUID id) {
        personServicePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
