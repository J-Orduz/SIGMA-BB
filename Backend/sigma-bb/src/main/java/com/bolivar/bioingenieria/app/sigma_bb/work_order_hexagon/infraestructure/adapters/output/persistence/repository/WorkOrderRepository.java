package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.repository;

import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.entity.WorkOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad WorkOrderEntity.
 * Proporciona operaciones CRUD estándar y consultas personalizadas.
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrderEntity, UUID> {

    /**
     * Busca todas las órdenes de trabajo activas asignadas a un ingeniero específico.
     *
     * @param identificadorIngeniero UUID del ingeniero.
     * @return Lista de órdenes activas del ingeniero.
     */
    List<WorkOrderEntity> findByIdentificadorIngenieroAndEstadoActivoTrue(UUID identificadorIngeniero);

    /**
     * Busca todas las órdenes de trabajo activas en el sistema.
     *
     * @return Lista de todas las órdenes activas.
     */
    List<WorkOrderEntity> findByEstadoActivoTrue();
}
