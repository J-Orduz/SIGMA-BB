package com.bolivar.bioingenieria.app.sigma_bb.shared.domain;

import java.time.Instant;

public record ReportRequest(String reportId, String modelId, Instant created) {
}
