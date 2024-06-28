package org.rivelles.cardefect.reports.ReportGenerationStrategy;

import org.rivelles.cardefect.domain.Defect;

import java.util.Set;

public interface ReportGeneratorStrategy {
    String generate(Set<Defect> defects);
}
