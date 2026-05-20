package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("b_estado_activo = true")
@SQLDelete(sql = "UPDATE ciudad SET b_estado_activo = false WHERE k_id_ciudad = ?")
@Table(name = "ciudad")
public class CityEntity {
    @Id
    @Column(name = "k_id_ciudad")
    private String id;

    @Column(name = "n_nombre_ciudad", nullable = false)
    private String name;

    @Column(name = "k_id_pais")
    private String countryId;

    @Column(name = "b_estado_activo", nullable = false)
    private Boolean active = true;
}
