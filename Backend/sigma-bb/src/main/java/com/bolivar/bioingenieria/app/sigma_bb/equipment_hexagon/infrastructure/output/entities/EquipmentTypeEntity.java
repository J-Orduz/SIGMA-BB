package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("b_estado_activo = true")
@SQLDelete(sql = "UPDATE tipo_equipo SET b_estado_activo = false WHERE k_id_tipo_equipo = ?")
@Table(name = "tipo_equipo")
public class EquipmentTypeEntity {
    @Id
    @Column(name = "k_id_tipo_equipo")
    private UUID id;

    @Column(name = "n_nombre_tipo_equipo", length = 50, nullable = false)
    private String equipmentTypeName;

    @Column(name = "t_definicion_tecnica", length = 250)
    private String technicalDefinition;

    @Column(name = "t_recomendaciones_cuidado", length = 250)
    private String careRecommendations;

    @Column(name = "i_voltage")
    private Integer voltage;

    @Column(name = "d_amperaje")
    private BigDecimal amperage;

    @Column(name = "t_tecnologia_predominante", length = 50)
    private String predominantTechnology;

    @Column(name = "b_verificable")
    private Boolean verifiable;

    @Column(name = "m_valor_unitario_mantenimiento")
    private Long unitMaintenanceValue;

    @Column(name = "b_estado_activo", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "equipmentType", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    private List<MetrologicalDataEntity> metrologicalDataEntities;

    @OneToMany(mappedBy = "equipmentTypeEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    private List<TechnicalVerificationEquipmentEntity> technicalVerificationEquipmentEntities;

    @Override
    public String toString() {
        return "EquipmentTypeEntity{" +
                "id=" + id +
                ", equipmentTypeName='" + equipmentTypeName + '\'' +
                ", technicalDefinition='" + technicalDefinition + '\'' +
                ", careRecommendations='" + careRecommendations + '\'' +
                ", voltage=" + voltage +
                ", amperage=" + amperage +
                ", predominantTechnology='" + predominantTechnology + '\'' +
                ", verifiable=" + verifiable +
                ", unitMaintenanceValue=" + unitMaintenanceValue +
                ", active=" + active +
                ", list" + metrologicalDataEntities +
                ", list" + technicalVerificationEquipmentEntities +
                '}';
    }
}
