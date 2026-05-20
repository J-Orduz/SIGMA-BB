package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.report;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.response.CountryResponse;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.ReportData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryDomainReportDTO implements ReportData {
    private CountryResponse country;
}
