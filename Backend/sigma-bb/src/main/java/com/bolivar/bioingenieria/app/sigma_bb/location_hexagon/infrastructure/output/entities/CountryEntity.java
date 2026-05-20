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
@SQLDelete(sql = "UPDATE pais SET b_estado_activo = false WHERE k_id_pais = ?")
@Table(name = "pais")
public class CountryEntity {
    @Id
    @Column(name = "k_id_pais")
    private String id;

    @Column(name = "n_nombre_pais", nullable = false)
    private String name;

    @Column(name = "b_estado_activo", nullable = false)
    private Boolean active = true;
}
