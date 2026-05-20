package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.report;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.*;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.ReportData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDomainReportDTO implements ReportData {
    private EquipmentReponse equipment;
    private EquipmentTypeResponse equipmentType;
    private BrandResponse brand;
    private ModelResponse model;
    private ManufacturerResponse manufacturer;
}
