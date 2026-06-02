package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla intermedia "orden_trabajo_equipo_cliente".
 * Permite asociar múltiples equipos del cliente a una misma orden de trabajo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orden_trabajo_equipo_cliente")
@IdClass(WorkOrderClientEquipmentEntity.WorkOrderClientEquipmentId.class)
public class WorkOrderClientEquipmentEntity {

    /**
     * Orden de trabajo a la que pertenece esta relación.
     * FK compuesta — parte 1.
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "k_id_orden_trabajo", nullable = false)
    private WorkOrderEntity workOrder;

    /**
     * Identificador del equipo del cliente intervenido en la orden.
     * FK compuesta — parte 2.
     */
    @Id
    @Column(name = "k_id_equipo_cliente", nullable = false)
    private UUID identificadorEquipoCliente;

    /**
     * Clase de clave primaria compuesta para la entidad intermedia.
     */
    public static class WorkOrderClientEquipmentId implements Serializable {
        private WorkOrderEntity workOrder;
        private UUID identificadorEquipoCliente;

        public WorkOrderClientEquipmentId() {}

        public WorkOrderClientEquipmentId(WorkOrderEntity workOrder, UUID identificadorEquipoCliente) {
            this.workOrder = workOrder;
            this.identificadorEquipoCliente = identificadorEquipoCliente;
        }
    }
}
