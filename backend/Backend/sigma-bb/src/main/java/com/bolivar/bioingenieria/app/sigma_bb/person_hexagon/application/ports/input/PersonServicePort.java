package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.EmailPerson;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.Person;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.PhonePerson;

import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada que define los servicios de la lógica de negocio para la gestión de {@link Person}.
 * Expone las operaciones que pueden ser utilizadas por los adaptadores y capas superiores.
 */
public interface PersonServicePort {

    /**
     * Busca una {@link Person} por su identificador único.
     *
     * @param id Identificador único de la {@link Person}
     * @return Objeto {@link Person} encontrado
     */
    Person findById(UUID id);

    /**
     * Obtiene la lista completa de todas las {@link Person} registradas.
     *
     * @return Lista de objetos {@link Person}
     */
    List<Person> findAll();

    /**
     * Crea una nueva {@link Person} en el sistema.
     *
     * @param person Objeto {@link Person} a crear
     * @return {@link Person} creada
     */
    Person save(Person person);

    /**
     * Actualiza la información de una {@link Person} existente.
     *
     * @param id Identificador único de la {@link Person} a actualizar
     * @param person Objeto {@link Person} con los datos nuevos
     * @return {@link Person} actualizada
     */
    Person update(UUID id, Person person);

    /**
     * Elimina una {@link Person} del sistema.
     *
     * @param id Identificador único de la {@link Person} a eliminar
     */
    void delete(UUID id);

    /**
     * Agrega un correo electrónico a una {@link Person} existente.
     *
     * @param personId Identificador único de la {@link Person}
     * @param email Objeto {@link EmailPerson} a agregar
     * @return {@link Person} actualizada
     */
    Person addEmail(UUID personId, EmailPerson email);

    /**
     * Agrega un teléfono a una {@link Person} existente.
     *
     * @param personId Identificador único de la {@link Person}
     * @param phone Objeto {@link PhonePerson} a agregar
     * @return {@link Person} actualizada
     */
    Person addPhone(UUID personId, PhonePerson phone);

    /**
     * Actualiza un correo electrónico asociado a una {@link Person}.
     *
     * @param personId Identificador único de la {@link Person}
     * @param emailId Identificador único del correo a actualizar
     * @param email Objeto {@link EmailPerson} con los nuevos datos
     * @return {@link Person} actualizada
     */
    Person updateEmail(UUID personId, UUID emailId, EmailPerson email);

    /**
     * Actualiza un teléfono asociado a una {@link Person}.
     *
     * @param personId Identificador único de la {@link Person}
     * @param phoneId Identificador único del teléfono a actualizar
     * @param phone Objeto {@link PhonePerson} con los nuevos datos
     * @return {@link Person} actualizada
     */
    Person updatePhone(UUID personId, UUID phoneId, PhonePerson phone);

    /**
     * Elimina un correo electrónico asociado a una {@link Person}.
     *
     * @param personId Identificador único de la {@link Person}
     * @param emailId Identificador único del correo a eliminar
     * @return {@link Person} actualizada
     */
    Person removeEmail(UUID personId, UUID emailId);

    /**
     * Elimina un teléfono asociado a una {@link Person}.
     *
     * @param personId Identificador único de la {@link Person}
     * @param phoneId Identificador único del teléfono a eliminar
     * @return {@link Person} actualizada
     */
    Person removePhone(UUID personId, UUID phoneId);
}
