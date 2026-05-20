package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.repository;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.HeadquarterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Interfaz repositorio para la persistencia de entidades {@link HeadquarterEntity}.
 *
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) estándar
 * para la gestión de sedes en la base de datos. Extiende JpaRepository
 * para aprovechar las funcionalidades que proporciona Spring Data JPA.
 */
public interface HeadquarterRepository extends JpaRepository<HeadquarterEntity, UUID> {
}
