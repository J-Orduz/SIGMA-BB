package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.persistence.PersonPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.Person;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.mapper.PersonPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia que implementa el puerto de salida {@link PersonPersistencePort}.
 *
 * Proporciona las operaciones de persistencia para la entidad Person, actuando como
 * intermediario entre la lógica de aplicación y el repositorio de datos. Utiliza
 * PersonPersistenceMapper para las conversiones entre modelos de dominio y entidades
 * de persistencia.
 */
@Component
@RequiredArgsConstructor
public class PersonPersistenceAdapter implements PersonPersistencePort {

    private final PersonRepository personRepository;
    private final PersonPersistenceMapper personPersistenceMapper;

    /**
     * Busca una persona por su identificador único.
     *
     * @param id Identificador único (UUID) de la persona a buscar
     * @return Optional conteniendo la persona si existe, Empty en caso contrario
     */
    @Override
    public Optional<Person> findById(UUID id) {
        return personRepository.findById(id)
                .map(personPersistenceMapper::toPerson);
    }

    /**
     * Obtiene la lista completa de todas las personas almacenadas en la base de datos.
     *
     * @return Lista de todos los modelos de dominio Person
     */
    @Override
    public List<Person> findAll() {
        return personPersistenceMapper.toPersonList(
                personRepository.findAll());
    }

    /**
     * Persiste una nueva persona o actualiza una existente en la base de datos.
     *
     * @param person Modelo de dominio Person a persistir
     * @return Modelo de dominio Person con los datos persistidos
     */
    @Override
    public Person save(Person person) {
        return personPersistenceMapper.toPerson(
                personRepository.save(
                        personPersistenceMapper.toPersonEntity(person)));
    }

    /**
     * Elimina una persona de la base de datos.
     *
     * @param person Modelo de dominio Person a eliminar
     */
    @Override
    public void delete(Person person) {
        personPersistenceMapper.toPerson(
                personRepository.save(
                        personPersistenceMapper.toPersonEntity(person)));
    }

    /**
     * Desactiva los registros de la tabla representante_legal que referencian a esta persona.
     *
     * @param personId Identificador único de la persona
     */
    @Override
    public void deactivateRepresentantesLegalesByPersonId(UUID personId) {
        personRepository.deactivateRepresentantesLegalesByPersonId(personId);
    }
}
