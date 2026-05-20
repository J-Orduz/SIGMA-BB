package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public record EquipmentPayload(String equipmentTypeId, String brandId) implements Payload {}
