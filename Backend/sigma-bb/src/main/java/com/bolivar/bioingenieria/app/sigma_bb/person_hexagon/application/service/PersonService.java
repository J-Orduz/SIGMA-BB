package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.service;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.PersonServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.request.EmailPersonCreateRequestUseCase;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.request.PersonCreateRequestUseCase;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.identity.response.PersonIdentityResponse;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.persistence.PersonPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.exception.PersonNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.EmailPerson;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.Person;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.PhonePerson;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.identity.PersonIdentityAdapter;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.utils.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Servicio de aplicación que implementa la lógica de negocio para la gestión de personas.
 * Coordina las operaciones entre los puertos de entrada, la lógica de dominio y la persistencia.
 */
@Service
@RequiredArgsConstructor
public class  PersonService implements PersonServicePort {

    private final PersonPersistencePort personPersistencePort;
    private final PersonIdentityAdapter personIdentityAdapter;

    // ---------------------------------------------------------------------------------
    // ------------------------------- METODOS DE PERSON -------------------------------
    // ---------------------------------------------------------------------------------

    /**
     * Obtiene una persona específica a partir de su identificador único.
     *
     * @param id Identificador único de la persona
     * @return Objeto {@link Person} encontrado
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public Person findById(UUID id) {
        return personPersistencePort.findById(id)
                .orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Obtiene la lista completa de todas las personas registradas en el sistema.
     *
     * @return Lista de objetos {@link Person}
     */
    @Override
    public List<Person> findAll() {
        return personPersistencePort.findAll();
    }

    /**
     * Crea una nueva persona en el sistema.
     * Asigna identificadores únicos a la persona y a todos sus teléfonos y correos asociados.
     *
     * @param person Objeto {@link Person} a crear
     * @return {@link Person} creado con identificadores asignados
     */
    @Override
    public Person save(Person person) {
        person.setIdentificador(UUID.randomUUID());
        person.setEstadoActivo(true);
        for(PhonePerson phone : person.getPhonePersonList()) {
            phone.setIdTelefonoPersona(UUID.randomUUID());
            phone.setEstadoActivo(true);
        }
        for(EmailPerson email : person.getEmailPersonList()) {
            email.setIdCorreoPersona(UUID.randomUUID());
            email.setEstadoActivo(true);
        }
        return personPersistencePort.save(person);
    }

    /**
     * Registra un nuevo ingeniero en el sistema, creando una identidad en Keycloak y guardando la información en la base de datos.
     * Si ocurre un error durante el proceso, se asegura de eliminar cualquier usuario creado en Keycloak para mantener la consistencia.
     *
     * @param personCreateRequestUseCase DTO que contiene la información necesaria para crear el ingeniero
     * @return {@link Person} creado con la información proporcionada
     */
    @Override
    public Person registerEngineer(PersonCreateRequestUseCase personCreateRequestUseCase) {
        String keycloakUserId = null;

        try {
            keycloakUserId = personIdentityAdapter.createUser(
                    personIdentityResponseBuilderFromRequest(personCreateRequestUseCase),
                    RoleType.ENGINEER);

            Person person = personBuilderFromRequest(personCreateRequestUseCase,
                    RoleType.ENGINEER,
                    UUID.fromString(keycloakUserId)
            );

            return personPersistencePort.save(person);

        } catch (Exception e) {

            if (keycloakUserId != null) {
                personIdentityAdapter.deleteUser(keycloakUserId);
            }

            throw e;
        }
    }

    /**
     * Registra un nuevo administrador en el sistema, creando una identidad en Keycloak y guardando la información en la base de datos.
     * Si ocurre un error durante el proceso, se asegura de eliminar cualquier usuario creado en Keycloak para mantener la consistencia.
     *
     * @param personCreateRequestUseCase DTO que contiene la información necesaria para crear el administrador
     * @return {@link Person} creado con la información proporcionada
     */
    @Override
    public Person registerAdmin(PersonCreateRequestUseCase personCreateRequestUseCase) {
        String keycloakUserId = null;

        try {
            keycloakUserId = personIdentityAdapter.createUser(
                    personIdentityResponseBuilderFromRequest(personCreateRequestUseCase),
                    RoleType.ADMIN);

            Person person = personBuilderFromRequest(personCreateRequestUseCase,
                    RoleType.ADMIN,
                    UUID.fromString(keycloakUserId)
            );

            return personPersistencePort.save(person);

        } catch (Exception e) {

            if (keycloakUserId != null) {
                personIdentityAdapter.deleteUser(keycloakUserId);
            }

            throw e;
        }
    }

    /**
     * Registra un nuevo CEO-Client en el sistema, creando una identidad en Keycloak y guardando la información en la base de datos.
     * Si ocurre un error durante el proceso, se asegura de eliminar cualquier usuario creado en Keycloak para mantener la consistencia.
     *
     * @param personCreateRequestUseCase DTO que contiene la información necesaria para crear el CEO-Client
     * @return {@link Person} creado con la información proporcionada
     */
    @Override
    public Person registerCEOClient(PersonCreateRequestUseCase personCreateRequestUseCase) {
        String keycloakUserId = null;

        try {
            keycloakUserId = personIdentityAdapter.createUser(
                    personIdentityResponseBuilderFromRequest(personCreateRequestUseCase),
                    RoleType.CEO_CLIENT);

            Person person = personBuilderFromRequest(personCreateRequestUseCase,
                    RoleType.CEO_CLIENT,
                    UUID.fromString(keycloakUserId)
            );

            return personPersistencePort.save(person);

        } catch (Exception e) {

            if (keycloakUserId != null) {
                personIdentityAdapter.deleteUser(keycloakUserId);
            }

            throw e;
        }
    }

    /**
     * Actualiza la información de una persona existente.
     *
     * @param id Identificador único de la persona a actualizar
     * @param person Objeto {@link Person} con los nuevos datos
     * @return {@link Person} actualizado
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public Person update(UUID id, Person person) {
        return personPersistencePort.findById(id)
                .map(existingPerson -> {
                    existingPerson.setCedula(person.getCedula());
                    existingPerson.setPrimerNombre(person.getPrimerNombre());
                    existingPerson.setSegundoNombre(person.getSegundoNombre());
                    existingPerson.setPrimerApellido(person.getPrimerApellido());
                    existingPerson.setSegundoApellido(person.getSegundoApellido());
                    existingPerson.setTipoPersona(person.getTipoPersona());
                    existingPerson.setSegundoTipoPersona(person.getSegundoTipoPersona());
                    
                    // Actualizar correos de manera segura para Hibernate
                    if (person.getEmailPersonList() != null) {
                        for (EmailPerson email : person.getEmailPersonList()) {
                            if (email.getIdCorreoPersona() == null) {
                                email.setIdCorreoPersona(UUID.randomUUID());
                            }
                            email.setEstadoActivo(true);
                        }
                        existingPerson.getEmailPersonList().clear();
                        existingPerson.getEmailPersonList().addAll(person.getEmailPersonList());
                    }
                    
                    // Actualizar teléfonos de manera segura para Hibernate
                    if (person.getPhonePersonList() != null) {
                        for (PhonePerson phone : person.getPhonePersonList()) {
                            if (phone.getIdTelefonoPersona() == null) {
                                phone.setIdTelefonoPersona(UUID.randomUUID());
                            }
                            phone.setEstadoActivo(true);
                        }
                        existingPerson.getPhonePersonList().clear();
                        existingPerson.getPhonePersonList().addAll(person.getPhonePersonList());
                    }
                    
                    return personPersistencePort.save(existingPerson);
                })
                .orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Elimina una persona del sistema cambiando su estado a inactivo.
     *
     * @param id Identificador único de la persona a eliminar
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public void delete(UUID id) {
        personPersistencePort.findById(id)
            .map(existingPerson -> {
                existingPerson.setEstadoActivo(false);
                
                if (existingPerson.getEmailPersonList() != null) {
                    for (EmailPerson email : existingPerson.getEmailPersonList()) {
                        email.setEstadoActivo(false);
                    }
                }
                
                if (existingPerson.getPhonePersonList() != null) {
                    for (PhonePerson phone : existingPerson.getPhonePersonList()) {
                        phone.setEstadoActivo(false);
                    }
                }
                
                String tipo = existingPerson.getTipoPersona();
                if ("ADMIN".equals(tipo) || "ENGINEER".equals(tipo) || "CEO_CLIENT".equals(tipo)) {
                    try {
                        personIdentityAdapter.deleteUser(existingPerson.getIdentificador().toString());
                    } catch (Exception e) {
                        System.err.println("Advertencia: No se pudo eliminar el usuario de Keycloak: " + e.getMessage());
                    }
                }
                
                Person savedPerson = personPersistencePort.save(existingPerson);
                personPersistencePort.deactivateRepresentantesLegalesByPersonId(id);
                return savedPerson;
            })
            .orElseThrow(PersonNotFoundException::new);
    }

    // ---------------------------------------------------------------------------------
    // ------------------------------- METODOS DE EMAIL -------------------------------
    // ---------------------------------------------------------------------------------

    /**
     * Agrega un correo electrónico a una persona existente.
     *
     * @param personId Identificador único de la persona
     * @param email Objeto {@link EmailPerson} a agregar
     * @return {@link Person} actualizado con el nuevo correo
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public Person addEmail(UUID personId, EmailPerson email) {
        return personPersistencePort.findById(personId)
                .map(existingPerson -> {
                    email.setIdCorreoPersona(UUID.randomUUID());
                    existingPerson.addEmail(email);
                    return personPersistencePort.save(existingPerson);
                })
                .orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Actualiza un correo electrónico específico asociado a una persona.
     *
     * @param personId Identificador único de la persona
     * @param emailId Identificador único del correo a actualizar
     * @param email Objeto {@link EmailPerson} con los nuevos datos
     * @return {@link Person} actualizado
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public Person updateEmail(UUID personId, UUID emailId, EmailPerson email) {
        return personPersistencePort.findById(personId)
                .map(existingPerson -> {
                    existingPerson.getEmailPersonList().stream()
                            .filter(e -> e.getIdCorreoPersona().equals(emailId))
                            .findFirst()
                            .ifPresent(e -> {
                                e.setCorreoPersona(email.getCorreoPersona());
                            });
                    return personPersistencePort.save(existingPerson);
                })
                .orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Elimina un correo electrónico específico asociado a una persona.
     *
     * @param personId Identificador único de la persona
     * @param emailId Identificador único del correo a eliminar
     * @return {@link Person} actualizado sin el correo eliminado
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public Person removeEmail(UUID personId, UUID emailId) {
        return personPersistencePort.findById(personId)
                .map(existingPerson -> {
                    existingPerson.removeEmail(emailId);
                    return personPersistencePort.save(existingPerson);
                })
                .orElseThrow(PersonNotFoundException::new);
    }

    // ---------------------------------------------------------------------------------
    // ------------------------------- METODOS DE PHONE -------------------------------
    // ---------------------------------------------------------------------------------

    /**
     * Agrega un teléfono a una persona existente.
     *
     * @param personId Identificador único de la persona
     * @param phone Objeto {@link PhonePerson} a agregar
     * @return {@link Person} actualizado con el nuevo teléfono
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public Person addPhone(UUID personId, PhonePerson phone) {
        return personPersistencePort.findById(personId)
                .map(existingPerson -> {
                    phone.setIdTelefonoPersona(UUID.randomUUID());
                    existingPerson.addPhone(phone);
                    return personPersistencePort.save(existingPerson);
                })
                .orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Actualiza un teléfono específico asociado a una persona.
     *
     * @param personId Identificador único de la persona
     * @param phoneId Identificador único del teléfono a actualizar
     * @param phone Objeto {@link PhonePerson} con los nuevos datos
     * @return {@link Person} actualizado
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public Person updatePhone(UUID personId, UUID phoneId, PhonePerson phone) {
        return personPersistencePort.findById(personId)
                .map(existingPerson -> {
                    existingPerson.getPhonePersonList().stream()
                            .filter(p -> p.getIdTelefonoPersona().equals(phoneId))
                            .findFirst()
                            .ifPresent(p -> {
                                p.setTelefonoPersona(phone.getTelefonoPersona());
                            });
                    return personPersistencePort.save(existingPerson);
                })
                .orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Elimina un teléfono específico asociado a una persona.
     *
     * @param personId Identificador único de la persona
     * @param phoneId Identificador único del teléfono a eliminar
     * @return {@link Person} actualizado sin el teléfono eliminado
     * @throws PersonNotFoundException Si la persona no existe en el sistema
     */
    @Override
    public Person removePhone(UUID personId, UUID phoneId) {
        return personPersistencePort.findById(personId)
                .map(existingPerson -> {
                    existingPerson.removePhone(phoneId);
                    return personPersistencePort.save(existingPerson);
                })
                .orElseThrow(PersonNotFoundException::new);
    }

    // ---------------------------------------------------------------------------------
    // -------------------------- METODOS DE ADICIONALES -------------------------------
    // ---------------------------------------------------------------------------------

        private Person personBuilderFromRequest(PersonCreateRequestUseCase personCreateRequestUseCase,
                                                RoleType roleType,
                                                UUID identificador) {
            return Person.builder()
                    .identificador(identificador)
                    .cedula(personCreateRequestUseCase.getCedula())
                    .primerNombre(personCreateRequestUseCase.getPrimerNombre())
                    .segundoNombre(personCreateRequestUseCase.getSegundoNombre())
                    .primerApellido(personCreateRequestUseCase.getPrimerApellido())
                    .segundoApellido(personCreateRequestUseCase.getSegundoApellido())
                    .tipoPersona(roleType.getName())
                    .estadoActivo(true)
                    .emailPersonList(personCreateRequestUseCase.getEmailPersonList()
                            .stream()
                            .map(emailRequest -> EmailPerson.builder()
                                    .idCorreoPersona(UUID.randomUUID())
                                    .correoPersona(emailRequest.getCorreoPersona())
                                    .estadoActivo(true)
                                    .build())
                            .toList())
                    .phonePersonList(personCreateRequestUseCase.getPhonePersonList()
                            .stream()
                            .map(phoneRequest -> PhonePerson.builder()
                                    .idTelefonoPersona(UUID.randomUUID())
                                    .telefonoPersona(phoneRequest.getTelefonoPersona())
                                    .estadoActivo(true)
                                    .build())
                            .toList())
                    .build();
        }

        private PersonIdentityResponse personIdentityResponseBuilderFromRequest(PersonCreateRequestUseCase personCreateRequestUseCase) {
            return PersonIdentityResponse.builder()
                    .userName(personCreateRequestUseCase.getNombreUsuario())
                    .email(personCreateRequestUseCase.getEmailPersonList()
                            .stream()
                            .findFirst()
                            .map(EmailPersonCreateRequestUseCase::getCorreoPersona)
                            .orElseThrow(() -> new IllegalArgumentException("No existe correo")))
                    .firstName(personCreateRequestUseCase.getPrimerNombre())
                    .lastName(personCreateRequestUseCase.getPrimerApellido())
                    .password(personCreateRequestUseCase.getPassword())
                    .build();
        }
}
