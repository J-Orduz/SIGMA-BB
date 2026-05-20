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
@SQLDelete(sql = "UPDATE fabricante SET b_estado_activo = false WHERE k_id_fabricante = ?")
@Table(name = "fabricante")
public class ManufacturerEntity {
    @Id
    @Column(name = "k_id_fabricante")
    private UUID id;

    @Column(name = "n_nombre_fabricante", nullable = false)
    private String name;

    @Column(name = "k_id_pais")
    private String countryId;

    @Column(name = "b_estado_activo", nullable = false)
    private Boolean active = true;
}
