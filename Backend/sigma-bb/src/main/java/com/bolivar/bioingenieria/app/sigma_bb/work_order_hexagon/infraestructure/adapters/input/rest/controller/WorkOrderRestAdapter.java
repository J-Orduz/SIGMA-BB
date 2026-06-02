package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.controller;

import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.application.ports.input.WorkOrderServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.mapper.WorkOrderRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.model.request.WorkOrderCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.model.request.WorkOrderStatusRequest;
import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.model.response.WorkOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Adaptador REST para la gestión de Órdenes de Trabajo.
 *
 * Expone los endpoints HTTP para el módulo de Work Orders,
 * actuando como punto de entrada desde el exterior hacia la aplicación.
 *
 * Roles:
 * - Administrador / SuperUsuario: CRUD completo.
 * - Ingeniero Técnico: consulta y cambio de estado.
 *
 * Sigue el principio de arquitectura hexagonal.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/work-order")
@Tag(name = "Work Order REST API", description = "Endpoints para la gestión de Órdenes de Trabajo")
public class WorkOrderRestAdapter {

    private final WorkOrderServicePort workOrderServicePort;
    private final WorkOrderRestMapper workOrderRestMapper;

    // ------------------------------------------------------------------
    // ----------------------- CONSULTAS (GET) --------------------------
    // ------------------------------------------------------------------

    /**
     * Obtiene todas las órdenes de trabajo activas del sistema.
     * Acceso: Administrador, SuperUsuario.
     *
     * @return Lista de órdenes de trabajo activas.
     */
    @Operation(
        summary = "Listar todas las órdenes de trabajo",
        description = "Devuelve todas las órdenes de trabajo activas registradas en el sistema."
    )
    @GetMapping("/v1/api")
    public List<WorkOrderResponse> getAllWorkOrders() {
        return workOrderRestMapper.toResponseList(workOrderServicePort.findAll());
    }

    /**
     * Obtiene una orden de trabajo específica por su UUID.
     * Acceso: Administrador, SuperUsuario, Ingeniero Técnico.
     *
     * @param workOrderId UUID de la orden de trabajo.
     * @return Detalles de la orden de trabajo.
     */
    @Operation(
        summary = "Obtener orden de trabajo por ID",
        description = "Devuelve los detalles de una orden de trabajo identificada por su UUID."
    )
    @GetMapping("/v1/api/{workOrderId}")
    public WorkOrderResponse getWorkOrderById(
            @Parameter(description = "UUID de la orden de trabajo", required = true)
            @PathVariable UUID workOrderId) {
        return workOrderRestMapper.toResponse(workOrderServicePort.findById(workOrderId));
    }

    /**
     * Obtiene todas las órdenes asignadas a un ingeniero específico.
     * Acceso: Administrador, SuperUsuario, Ingeniero Técnico (sus propias órdenes).
     *
     * @param engineerId UUID del ingeniero.
     * @return Lista de órdenes del ingeniero.
     */
    @Operation(
        summary = "Listar órdenes por ingeniero",
        description = "Devuelve todas las órdenes de trabajo activas asignadas a un ingeniero específico."
    )
    @GetMapping("/v1/api/engineer/{engineerId}")
    public List<WorkOrderResponse> getWorkOrdersByEngineer(
            @Parameter(description = "UUID del ingeniero", required = true)
            @PathVariable UUID engineerId) {
        return workOrderRestMapper.toResponseList(workOrderServicePort.findByEngineerId(engineerId));
    }

    // ------------------------------------------------------------------
    // ----------------------- CREACIÓN (POST) --------------------------
    // ------------------------------------------------------------------

    /**
     * Crea una nueva orden de trabajo.
     * Acceso: Administrador, SuperUsuario.
     *
     * @param request DTO con los datos de la nueva orden.
     * @return La orden creada con estado HTTP 201.
     */
    @Operation(
        summary = "Crear nueva orden de trabajo",
        description = "Crea una nueva orden de trabajo con los equipos del cliente seleccionados."
    )
    @PostMapping("/v1/api")
    public ResponseEntity<WorkOrderResponse> createWorkOrder(
            @Valid @RequestBody WorkOrderCreateRequest request) {
        WorkOrderResponse response = workOrderRestMapper.toResponse(
                workOrderServicePort.save(workOrderRestMapper.toDomain(request))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ------------------------------------------------------------------
    // ----------------------- ACTUALIZACIÓN (PUT/PATCH) ----------------
    // ------------------------------------------------------------------

    /**
     * Actualiza completamente una orden de trabajo existente.
     * Acceso: Administrador, SuperUsuario.
     *
     * @param workOrderId UUID de la orden a actualizar.
     * @param request     DTO con los nuevos datos.
     * @return La orden actualizada.
     */
    @Operation(
        summary = "Actualizar orden de trabajo",
        description = "Actualiza todos los campos de una orden de trabajo existente."
    )
    @PutMapping("/v1/api/{workOrderId}")
    public ResponseEntity<WorkOrderResponse> updateWorkOrder(
            @Parameter(description = "UUID de la orden a actualizar", required = true)
            @PathVariable UUID workOrderId,
            @Valid @RequestBody WorkOrderCreateRequest request) {
        WorkOrderResponse response = workOrderRestMapper.toResponse(
                workOrderServicePort.update(workOrderId, workOrderRestMapper.toDomain(request))
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza únicamente el estado de ejecución de una orden de trabajo.
     * Permite la transición: CREATED → IN_PROGRESS → EXECUTED.
     * Acceso: Administrador, SuperUsuario, Ingeniero Técnico.
     *
     * @param workOrderId UUID de la orden.
     * @param request     DTO con el nuevo estado.
     * @return La orden con el estado actualizado.
     */
    @Operation(
        summary = "Actualizar estado de ejecución",
        description = "Cambia el estado de ejecución de la orden: CREATED → IN_PROGRESS → EXECUTED."
    )
    @PatchMapping("/v1/api/{workOrderId}/status")
    public ResponseEntity<WorkOrderResponse> updateWorkOrderStatus(
            @Parameter(description = "UUID de la orden", required = true)
            @PathVariable UUID workOrderId,
            @Valid @RequestBody WorkOrderStatusRequest request) {
        WorkOrderResponse response = workOrderRestMapper.toResponse(
                workOrderServicePort.updateStatus(workOrderId, request.getEstadoEjecucion())
        );
        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------
    // ----------------------- ELIMINACIÓN (DELETE) ---------------------
    // ------------------------------------------------------------------

    /**
     * Elimina lógicamente una orden de trabajo (soft delete).
     * Acceso: Administrador, SuperUsuario.
     *
     * @param workOrderId UUID de la orden a eliminar.
     * @return Respuesta 204 No Content si fue exitoso.
     */
    @Operation(
        summary = "Eliminar orden de trabajo",
        description = "Realiza un soft-delete de la orden de trabajo (b_estado_activo = false)."
    )
    @DeleteMapping("/v1/api/{workOrderId}")
    public ResponseEntity<Void> deleteWorkOrder(
            @Parameter(description = "UUID de la orden a eliminar", required = true)
            @PathVariable UUID workOrderId) {
        workOrderServicePort.delete(workOrderId);
        return ResponseEntity.noContent().build();
    }
}
