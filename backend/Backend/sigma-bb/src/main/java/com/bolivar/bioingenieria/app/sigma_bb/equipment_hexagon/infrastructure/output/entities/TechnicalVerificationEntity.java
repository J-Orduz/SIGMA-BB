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
@SQLDelete(sql = "UPDATE verificacion_tecnica SET b_estado_activo = false WHERE k_id_verificacion = ?")
@Table(name = "verificacion_tecnica")
public class TechnicalVerificationEntity {
    @Id
    @Column(name = "k_id_verificacion")
    private UUID id;

    @Column(name = "t_descripcion", length = 250, nullable = false)
    private String description;

    @Column(name = "t_tipo_verificacion", length = 25, nullable = false)
    private String verificationType;

    @Column(name = "b_estado_activo", nullable = false)
    private Boolean active = true;
}
