package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("b_estado_activo = true")
@SQLDelete(sql = "UPDATE equipo SET b_estado_activo = false WHERE k_id_equipo = ?")
@Table(name = "equipo")
public class EquipmentEntity {
    @Id
    @Column(name = "k_id_equipo")
    private UUID id;

    @Column(name = "k_id_tipo_equipo", nullable = false)
    private UUID equipmentTypeId;

    @Column(name = "k_id_marca", nullable = false)
    private UUID brandId;

    @Column(name = "b_estado_activo", nullable = false)
    private Boolean active = true;
}
