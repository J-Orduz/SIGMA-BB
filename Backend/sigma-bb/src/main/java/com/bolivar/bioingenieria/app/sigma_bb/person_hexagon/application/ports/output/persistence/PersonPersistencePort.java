package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.persistence;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para la persistencia de {@link Person}.
 * Define las operaciones básicas de acceso a datos que debe implementar la infraestructura.
 */
public interface PersonPersistencePort {

    /**
     * Busca una {@link Person} por su identificador único.
     *
     * @param id Identificador único de la {@link Person}
     * @return Opcional con la {@link Person} encontrada
     */
    Optional<Person> findById(UUID id);

    /**
     * Obtiene la lista completa de {@link Person} almacenadas.
     *
     * @return Lista de {@link Person}
     */
    List<Person> findAll();

    /**
     * Guarda o actualiza una {@link Person} en el almacenamiento.
     *
     * @param person Objeto {@link Person} a persistir
     * @return {@link Person} guardada
     */
    Person save(Person person);

    /**
     * Elimina una {@link Person} del almacenamiento.
     *
     * @param person Objeto {@link Person} a eliminar
     */
    void delete(Person person);

    /**
     * Desactiva los registros de la tabla representante_legal que referencian a esta persona.
     *
     * @param personId Identificador único de la persona
     */
    void deactivateRepresentantesLegalesByPersonId(UUID personId);
}
