package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.services.metrological_data_services.commands;

import java.math.BigDecimal;

public record MetrologicalDataCommand(BigDecimal value, String type) {
}
