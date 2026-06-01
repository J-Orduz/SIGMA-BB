package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reporte_servicio")
public class ServiceReportEntity {

    @Id
    @Column(name = "k_id_reporte_servicio")
    private UUID id;

    @Column(name = "k_id_orden_trabajo", nullable = false, length = 10)
    private String workOrderId;

    @Column(name = "k_id_equipo_cliente", nullable = false, length = 10)
    private String clientEquipmentId;

    @Column(name = "t_observaciones_reporte_servico", length = 250)
    private String observations;

    @Column(name = "t_resultado_verificacion_tecnica", length = 50)
    private String technicalVerificationResult;
}
