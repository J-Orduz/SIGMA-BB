package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.listeners;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.input.CountryServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.mapper.CountryRestMapper;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.report.CountryDomainReportDTO;
import com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.input.ReportDataProviderPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("countryReportProvider")
public class CountryReportProviderAdapter implements ReportDataProviderPort<CountryDomainReportDTO> {

    private final CountryServicePort countryServicePort;
    private final CountryRestMapper countryRestMapper;

    @Autowired
    public CountryReportProviderAdapter(CountryServicePort countryServicePort,
                                         CountryRestMapper countryRestMapper) {
        this.countryServicePort = countryServicePort;
        this.countryRestMapper = countryRestMapper;
    }

    @Override
    public String domainName() {
        return "country-domain";
    }

    @Override
    public CountryDomainReportDTO provideReportData(String countryId) {
        var country = countryServicePort.findById(countryId);

        return CountryDomainReportDTO.builder()
                .country(countryRestMapper.toCountryResponse(country))
                .build();
    }
}
