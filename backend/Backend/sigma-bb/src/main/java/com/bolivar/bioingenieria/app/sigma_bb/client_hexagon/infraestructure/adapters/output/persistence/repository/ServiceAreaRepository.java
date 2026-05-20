package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.repository;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ServiceAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Interfaz repositorio para la persistencia de entidades {@link ServiceAreaEntity}.
 *
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) estándar
 * para la gestión de áreas de servicio en la base de datos. Extiende JpaRepository
 * para aprovechar las funcionalidades que proporciona Spring Data JPA.
 */
public interface ServiceAreaRepository extends JpaRepository<ServiceAreaEntity, UUID> {
}
