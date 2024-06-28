package org.rivelles.cardefect.domain;

import org.rivelles.cardefect.reports.ReportGenerationStrategy.ReportGeneratorStrategy;

import java.util.Set;

public class Manager extends User {
    protected Manager(String id, String name) {
        super(id, name);
    }

    public String generateReport(Set<Defect> defects, ReportGeneratorStrategy strategy) {
        return strategy.generate(defects);
    }
}
