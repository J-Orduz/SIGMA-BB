package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetrologicalDataId implements Serializable {

    @Column(name = "d_valor", precision = 10, scale = 4, nullable = false)
    private BigDecimal value;

    @Column(name = "n_tipo", length = 50, nullable = false)
    private String type;

    @Column(name = "k_id_tipo_equipo")
    private UUID equipmentTypeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetrologicalDataId that = (MetrologicalDataId) o;
        return value != null ? value.equals(that.value) : that.value == null &&
                type != null ? type.equals(that.type) : that.type == null &&
                equipmentTypeId != null ? equipmentTypeId.equals(that.equipmentTypeId) : that.equipmentTypeId == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (equipmentTypeId != null ? equipmentTypeId.hashCode() : 0);
        return result;
    }
}
