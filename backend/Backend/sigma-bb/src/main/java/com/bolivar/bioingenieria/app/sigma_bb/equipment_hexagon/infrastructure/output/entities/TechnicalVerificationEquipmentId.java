package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalVerificationEquipmentId implements Serializable {
    @Column(name = "k_id_verificacion", nullable = false)
    private UUID verificationId;

    @Column(name = "k_id_tipo_equipo", nullable = false)
    private UUID typeEquipmentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicalVerificationEquipmentId that = (TechnicalVerificationEquipmentId) o;
        return verificationId != null ? verificationId.equals(that.verificationId) : that.verificationId == null &&
                typeEquipmentId != null ? typeEquipmentId.equals(that.typeEquipmentId) : that.typeEquipmentId == null;
    }

    @Override
    public int hashCode() {
        int result = verificationId != null ? verificationId.hashCode() : 0;
        result = 31 * result + (typeEquipmentId != null ? typeEquipmentId.hashCode() : 0);
        return result;
    }
}
