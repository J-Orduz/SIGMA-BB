package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.adapters;

import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.application.ports.output.WorkOrderPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.domain.model.WorkOrder;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.entity.WorkOrderEntity;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.mapper.WorkOrderPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para Órdenes de Trabajo.
 * Implementa el puerto de salida delegando las operaciones al repositorio JPA.
 * Sigue el principio de arquitectura hexagonal.
 */
@Component
@RequiredArgsConstructor
public class WorkOrderPersistenceAdapter implements WorkOrderPersistencePort {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderPersistenceMapper workOrderPersistenceMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkOrder> findAll() {
        return workOrderPersistenceMapper.toDomainList(
                workOrderRepository.findByEstadoActivoTrue()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<WorkOrder> findById(UUID id) {
        return workOrderRepository.findById(id)
                .map(workOrderPersistenceMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkOrder> findByEngineerId(UUID engineerId) {
        return workOrderPersistenceMapper.toDomainList(
                workOrderRepository.findByIdentificadorIngenieroAndEstadoActivoTrue(engineerId)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkOrder save(WorkOrder workOrder) {
        WorkOrderEntity entity = workOrderPersistenceMapper.toEntity(workOrder);
        WorkOrderEntity saved = workOrderRepository.save(entity);
        return workOrderPersistenceMapper.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     * Actualiza todos los campos de la orden existente, incluyendo las relaciones
     * con equipos del cliente (la cascada se encarga de limpiar y recrear).
     */
    @Override
    public WorkOrder update(UUID id, WorkOrder workOrder) {
        WorkOrderEntity existing = workOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada: " + id));

        existing.setFechaMantenimiento(workOrder.getFechaMantenimiento());
        existing.setPeriodicidad(workOrder.getPeriodicidad());
        existing.setIdentificadorIngeniero(workOrder.getIdentificadorIngeniero());
        existing.setEstadoEjecucion(workOrder.getEstadoEjecucion());
        existing.setEstadoActivo(workOrder.isEstadoActivo());

        // Actualizar relaciones con equipos (limpia y reconstruye gracias a orphanRemoval)
        existing.getClientEquipmentRelations().clear();
        if (workOrder.getEquipoClienteIds() != null) {
            workOrder.getEquipoClienteIds().forEach(equipoId -> {
                var rel = new com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon
                        .infraestructure.adapters.output.persistence.entity
                        .WorkOrderClientEquipmentEntity();
                rel.setWorkOrder(existing);
                rel.setIdentificadorEquipoCliente(equipoId);
                existing.getClientEquipmentRelations().add(rel);
            });
        }

        return workOrderPersistenceMapper.toDomain(workOrderRepository.save(existing));
    }

    /**
     * {@inheritDoc}
     * Soft delete: marca b_estado_activo = false.
     */
    @Override
    public void delete(UUID id) {
        WorkOrderEntity entity = workOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada: " + id));
        entity.setEstadoActivo(false);
        workOrderRepository.save(entity);
    }
}
