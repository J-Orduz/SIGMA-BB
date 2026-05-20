package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.Person;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.entity.PersonEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link Person}
 * y su representación de persistencia {@link PersonEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas. Integra además los
 * mapeadores de {@link EmailPersonPersistenceMapper} y {@link PhonePersonPersistenceMapper}
 * para manejar las relaciones de uno a muchos con correos y teléfonos.
 */
@Mapper(componentModel = "spring", uses = {
        EmailPersonPersistenceMapper.class,
        PhonePersonPersistenceMapper.class})
public interface PersonPersistenceMapper {

    /**
     * Convierte un modelo de dominio Person a su representación de entidad PersonEntity.
     *
     * @param person Objeto de dominio con la información de la persona
     * @return Entidad PersonEntity lista para persistencia
     */
    PersonEntity toPersonEntity(Person person);

    /**
     * Convierte una entidad de persistencia PersonEntity a su modelo de dominio Person.
     *
     * @param personEntity Entidad de persistencia con la información de la persona
     * @return Modelo de dominio Person con los datos recuperados
     */
    Person toPerson(PersonEntity personEntity);

    /**
     * Convierte una lista de entidades de persistencia PersonEntity a una lista
     * de modelos de dominio Person.
     *
     * @param personEntities Lista de entidades de persistencia con información de personas
     * @return Lista de modelos de dominio Person
     */
    List<Person> toPersonList(List<PersonEntity> personEntities);

    /**
     * Vincula automáticamente las relaciones bidireccionales entre la entidad Person
     * y sus entidades relacionadas (EmailPersonEntity y PhonePersonEntity) después
     * del mapeo inicial.
     *
     * Este método se ejecuta después del mapeo para asegurar que las referencias
     * inversas se establezcan correctamente en la base de datos.
     *
     * @param personEntity Entidad PersonEntity con las relaciones a vincular
     */
    @AfterMapping
    default void linkChildren(@MappingTarget PersonEntity personEntity) {
        if (personEntity.getEmailPersonList() != null) {
            personEntity.getEmailPersonList()
                    .forEach(emailPersonEntity ->
                            emailPersonEntity.setPerson(personEntity));
        }
        if (personEntity.getPhonePersonList() != null) {
            personEntity.getPhonePersonList()
                    .forEach(phonePerson ->
                            phonePerson.setPerson(personEntity));
        }
    }
}
