package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.repository;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Interfaz repositorio para la persistencia de entidades {@link PersonEntity}.
 *
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) estándar
 * para la gestión de personas en la base de datos. Extiende JpaRepository
 * para aprovechar las funcionalidades que proporciona Spring Data JPA.
 */
public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {
}
