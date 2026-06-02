package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.domain.model.WorkOrder;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.model.request.WorkOrderCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.model.response.WorkOrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre los DTOs REST y el modelo de dominio WorkOrder.
 * Desacopla la capa REST del dominio de negocio.
 */
@Component
public class WorkOrderRestMapper {

    /**
     * Convierte un DTO de creación/actualización al modelo de dominio.
     *
     * @param request DTO de entrada.
     * @return Modelo de dominio WorkOrder.
     */
    public WorkOrder toDomain(WorkOrderCreateRequest request) {
        if (request == null) return null;

        WorkOrder workOrder = new WorkOrder();
        workOrder.setFechaMantenimiento(request.getFechaMantenimiento());
        workOrder.setPeriodicidad(request.getPeriodicidad());
        workOrder.setIdentificadorIngeniero(request.getIdentificadorIngeniero());
        workOrder.setEquipoClienteIds(request.getEquipoClienteIds());

        return workOrder;
    }

    /**
     * Convierte un modelo de dominio al DTO de respuesta.
     *
     * @param workOrder Modelo de dominio.
     * @return DTO de respuesta WorkOrderResponse.
     */
    public WorkOrderResponse toResponse(WorkOrder workOrder) {
        if (workOrder == null) return null;

        WorkOrderResponse response = new WorkOrderResponse();
        response.setIdentificadorOrdenTrabajo(workOrder.getIdentificadorOrdenTrabajo());
        response.setFechaMantenimiento(workOrder.getFechaMantenimiento());
        response.setPeriodicidad(workOrder.getPeriodicidad());
        response.setIdentificadorIngeniero(workOrder.getIdentificadorIngeniero());
        response.setEstadoEjecucion(workOrder.getEstadoEjecucion());
        response.setEstadoActivo(workOrder.isEstadoActivo());
        response.setEquipoClienteIds(workOrder.getEquipoClienteIds());

        return response;
    }

    /**
     * Convierte una lista de modelos de dominio a DTOs de respuesta.
     *
     * @param workOrders Lista de modelos de dominio.
     * @return Lista de DTOs de respuesta.
     */
    public List<WorkOrderResponse> toResponseList(List<WorkOrder> workOrders) {
        if (workOrders == null) return List.of();
        return workOrders.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
