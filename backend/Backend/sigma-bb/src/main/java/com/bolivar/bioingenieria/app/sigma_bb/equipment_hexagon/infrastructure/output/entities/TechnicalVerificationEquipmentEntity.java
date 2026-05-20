package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verificacion_tecnica_tipo_equipo")
public class TechnicalVerificationEquipmentEntity {
    @EmbeddedId
    private TechnicalVerificationEquipmentId tecnicalVerificationEquipmentId;

    @Column(name = "b_estado_activo", nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "k_id_tipo_equipo", insertable = false, updatable = false)
    private EquipmentTypeEntity equipmentTypeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "k_id_verificacion", insertable = false, updatable = false)
    private TechnicalVerificationEntity technicalVerificationEntity;
}
