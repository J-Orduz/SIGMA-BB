package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment;

import lombok.*;

import java.util.UUID;

/*
* Esa clase se utilizará UNICAMENTE para transportar la información de un equipo de verificación técnica,
* no es una entidad ni un agregado, es un simple DTO que se usará en los eventos y en las respuestas de la API.
* */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TechnicalVerificationEquipment {
    //private UUID equipmentTypeId;
    private UUID technicalVerificationId;
    private String description;
    private String verificationType;
}
