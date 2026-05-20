package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelResponse {

    private UUID id;
    private String invima;
    private UUID manufacturerId;
    private UUID equipmentId;
}
