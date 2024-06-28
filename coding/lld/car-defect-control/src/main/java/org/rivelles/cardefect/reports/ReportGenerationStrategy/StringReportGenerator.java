package org.rivelles.cardefect.reports.ReportGenerationStrategy;

import org.rivelles.cardefect.domain.Defect;

import java.util.Set;
import java.util.stream.Collectors;

public class StringReportGenerator implements ReportGeneratorStrategy {
    @Override
    public String generate(Set<Defect> defects) {
        return defects
            .stream()
            .sorted()
            .map(Defect::toString)
            .collect(Collectors.joining("\n"));
    }
}
