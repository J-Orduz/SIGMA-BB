package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetrologicalData implements Payload {
    private BigDecimal value;
    private String type;
}
