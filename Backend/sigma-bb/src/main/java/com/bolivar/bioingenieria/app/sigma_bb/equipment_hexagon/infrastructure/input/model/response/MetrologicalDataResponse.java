package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetrologicalDataResponse {
    private BigDecimal value;
    private String type;
}
