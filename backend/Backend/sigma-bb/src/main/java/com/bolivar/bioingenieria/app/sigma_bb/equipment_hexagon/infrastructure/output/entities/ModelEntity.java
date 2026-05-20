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
@SQLDelete(sql = "UPDATE modelo SET b_estado_activo = false WHERE k_id_modelo = ?")
@Table(name = "modelo")
public class ModelEntity {

    @Id
    @Column(name = "k_id_modelo")
    private UUID id;

    @Column(name = "n_invima", nullable = false)
    private String invima;

    @Column(name = "k_id_fabricante")
    private UUID manufacturerId;

    @Column(name = "k_id_equipo")
    private UUID equipmentId;

    @Column(name = "b_estado_activo", nullable = false)
    private Boolean active = true;
}
