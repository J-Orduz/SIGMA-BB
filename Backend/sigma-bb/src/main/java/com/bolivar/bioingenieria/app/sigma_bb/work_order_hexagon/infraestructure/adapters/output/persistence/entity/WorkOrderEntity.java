package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla "orden_trabajo" en la base de datos.
 * Almacena las órdenes de mantenimiento preventivo/correctivo
 * de equipos clínicos para los clientes del sistema.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orden_trabajo")
public class WorkOrderEntity {

    /**
     * Identificador único de la orden de trabajo (PK, UUID).
     */
    @Id
    @Column(name = "k_id_orden_trabajo", nullable = false, unique = true)
    private UUID identificadorOrdenTrabajo;

    /**
     * Fecha y hora programada para el mantenimiento.
     */
    @Column(name = "f_fecha_mantenimiento", nullable = false)
    private LocalDateTime fechaMantenimiento;

    /**
     * Periodicidad del mantenimiento.
     * Valores permitidos: MONTHLY, QUARTERLY, BIANNUAL, ANUAL
     */
    @Column(name = "n_periodicidad", nullable = false, length = 15)
    private String periodicidad;

    /**
     * Identificador del ingeniero responsable de la orden.
     * FK hacia la tabla persona.
     */
    @Column(name = "k_identificador")
    private UUID identificadorIngeniero;

    /**
     * Estado de ejecución de la orden.
     * Valores permitidos: CREATED, IN_PROGRESS, EXECUTED
     */
    @Column(name = "t_estado_ejecucion", nullable = false, length = 15)
    private String estadoEjecucion = "CREATED";

    /**
     * Indica si la orden está activa en el sistema (soft delete).
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo = true;

    /**
     * Lista de relaciones con equipos del cliente asociados a esta orden.
     * Relación uno a muchos con la tabla intermedia "orden_trabajo_equipo_cliente".
     */
    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<WorkOrderClientEquipmentEntity> clientEquipmentRelations = new ArrayList<>();
}
