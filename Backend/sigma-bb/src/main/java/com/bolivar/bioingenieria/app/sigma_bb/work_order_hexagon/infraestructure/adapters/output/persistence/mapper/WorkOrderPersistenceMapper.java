package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.domain.model.WorkOrder;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.entity.WorkOrderClientEquipmentEntity;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.entity.WorkOrderEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre el modelo de dominio WorkOrder
 * y la entidad JPA WorkOrderEntity.
 */
@Component
public class WorkOrderPersistenceMapper {

    /**
     * Convierte una entidad JPA a un modelo de dominio.
     *
     * @param entity Entidad JPA a convertir.
     * @return Modelo de dominio WorkOrder.
     */
    public WorkOrder toDomain(WorkOrderEntity entity) {
        if (entity == null) return null;

        List<UUID> equipoClienteIds = new ArrayList<>();
        if (entity.getClientEquipmentRelations() != null) {
            equipoClienteIds = entity.getClientEquipmentRelations().stream()
                    .map(WorkOrderClientEquipmentEntity::getIdentificadorEquipoCliente)
                    .collect(Collectors.toList());
        }

        WorkOrder workOrder = new WorkOrder();
        workOrder.setIdentificadorOrdenTrabajo(entity.getIdentificadorOrdenTrabajo());
        workOrder.setFechaMantenimiento(entity.getFechaMantenimiento());
        workOrder.setPeriodicidad(entity.getPeriodicidad());
        workOrder.setIdentificadorIngeniero(entity.getIdentificadorIngeniero());
        workOrder.setEstadoEjecucion(entity.getEstadoEjecucion());
        workOrder.setEstadoActivo(entity.isEstadoActivo());
        workOrder.setEquipoClienteIds(equipoClienteIds);

        return workOrder;
    }

    /**
     * Convierte un modelo de dominio a una entidad JPA.
     * Construye también las relaciones intermedias con equipo_cliente.
     *
     * @param workOrder Modelo de dominio a convertir.
     * @return Entidad JPA WorkOrderEntity.
     */
    public WorkOrderEntity toEntity(WorkOrder workOrder) {
        if (workOrder == null) return null;

        WorkOrderEntity entity = new WorkOrderEntity();
        entity.setIdentificadorOrdenTrabajo(workOrder.getIdentificadorOrdenTrabajo());
        entity.setFechaMantenimiento(workOrder.getFechaMantenimiento());
        entity.setPeriodicidad(workOrder.getPeriodicidad());
        entity.setIdentificadorIngeniero(workOrder.getIdentificadorIngeniero());
        entity.setEstadoEjecucion(workOrder.getEstadoEjecucion());
        entity.setEstadoActivo(workOrder.isEstadoActivo());

        // Construir relaciones intermedias
        if (workOrder.getEquipoClienteIds() != null) {
            List<WorkOrderClientEquipmentEntity> relations = workOrder.getEquipoClienteIds().stream()
                    .map(equipoId -> {
                        WorkOrderClientEquipmentEntity rel = new WorkOrderClientEquipmentEntity();
                        rel.setWorkOrder(entity);
                        rel.setIdentificadorEquipoCliente(equipoId);
                        return rel;
                    })
                    .collect(Collectors.toList());
            entity.setClientEquipmentRelations(relations);
        }

        return entity;
    }

    /**
     * Convierte una lista de entidades JPA a modelos de dominio.
     *
     * @param entities Lista de entidades a convertir.
     * @return Lista de modelos de dominio.
     */
    public List<WorkOrder> toDomainList(List<WorkOrderEntity> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }
}
