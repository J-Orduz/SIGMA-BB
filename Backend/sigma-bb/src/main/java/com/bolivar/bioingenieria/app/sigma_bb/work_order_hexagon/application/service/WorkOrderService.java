package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.application.service;

import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.application.ports.input.WorkOrderServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.application.ports.output.WorkOrderPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.domain.model.WorkOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de Órdenes de Trabajo.
 * Orquesta la lógica de negocio delegando la persistencia al puerto de salida.
 * Sigue el principio de arquitectura hexagonal.
 */
@Service
@RequiredArgsConstructor
public class WorkOrderService implements WorkOrderServicePort {

    private final WorkOrderPersistencePort workOrderPersistencePort;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkOrder> findAll() {
        return workOrderPersistencePort.findAll();
    }

    /**
     * {@inheritDoc}
     * Lanza una excepción si la orden no existe.
     */
    @Override
    public WorkOrder findById(UUID id) {
        return workOrderPersistencePort.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada con ID: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkOrder> findByEngineerId(UUID engineerId) {
        return workOrderPersistencePort.findByEngineerId(engineerId);
    }

    /**
     * {@inheritDoc}
     * Genera un UUID nuevo y establece el estado inicial CREATED.
     */
    @Override
    public WorkOrder save(WorkOrder workOrder) {
        workOrder.setIdentificadorOrdenTrabajo(UUID.randomUUID());
        workOrder.setEstadoEjecucion("CREATED");
        workOrder.setEstadoActivo(true);
        return workOrderPersistencePort.save(workOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkOrder update(UUID id, WorkOrder workOrder) {
        // Verificar que la orden existe antes de actualizar
        findById(id);
        return workOrderPersistencePort.update(id, workOrder);
    }

    /**
     * {@inheritDoc}
     * Valida que la transición de estado sea válida:
     * CREATED → IN_PROGRESS → EXECUTED
     */
    @Override
    public WorkOrder updateStatus(UUID id, String estadoEjecucion) {
        WorkOrder existing = findById(id);

        // Validar transición de estados
        String current = existing.getEstadoEjecucion();
        boolean validTransition = switch (estadoEjecucion) {
            case "IN_PROGRESS" -> "CREATED".equals(current);
            case "EXECUTED" -> "IN_PROGRESS".equals(current);
            case "CREATED" -> false; // No se puede regresar a CREATED
            default -> false;
        };

        if (!validTransition) {
            throw new RuntimeException(
                "Transición de estado inválida: " + current + " → " + estadoEjecucion
            );
        }

        existing.setEstadoEjecucion(estadoEjecucion);
        return workOrderPersistencePort.update(id, existing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID id) {
        // Verificar que la orden existe antes de eliminar
        findById(id);
        workOrderPersistencePort.delete(id);
    }
}
