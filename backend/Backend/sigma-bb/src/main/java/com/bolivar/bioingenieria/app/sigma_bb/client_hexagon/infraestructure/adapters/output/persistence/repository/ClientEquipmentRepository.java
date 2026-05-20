package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.repository;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ClientEquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Interfaz repositorio para la persistencia de entidades {@link ClientEquipmentEntity}.
 *
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) estándar
 * para la gestión de equipos de clientes en la base de datos. Extiende JpaRepository
 * para aprovechar las funcionalidades que proporciona Spring Data JPA.
 */
public interface ClientEquipmentRepository extends JpaRepository<ClientEquipmentEntity, UUID> {
}
