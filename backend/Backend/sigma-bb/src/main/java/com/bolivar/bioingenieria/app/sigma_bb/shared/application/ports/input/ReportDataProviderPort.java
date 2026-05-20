package com.bolivar.bioingenieria.app.sigma_bb.shared.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.ReportData;

public interface ReportDataProviderPort<T extends ReportData> {
    String domainName();
    T provideReportData(String id);
}
