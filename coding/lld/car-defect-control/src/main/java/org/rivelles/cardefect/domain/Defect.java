package org.rivelles.cardefect.domain;

import org.rivelles.cardefect.domain.exceptions.DefectAlreadySolvedException;
import org.rivelles.cardefect.domain.exceptions.UndefinedUrgencyException;

import java.time.Instant;

import static org.rivelles.cardefect.domain.DefectUrgency.HIGH;

public class Defect implements Comparable<Defect> {
    private DefectUrgency urgency;

    private String description;
    private String solution;
    private final Instant createdAt;
    private Instant solvedAt;
    private Operator assignedTo;
    private final Operator reportedBy;

    Defect(DefectUrgency urgency, String description, Operator reporter) {
        this.urgency = urgency;
        this.description = description;
        this.reportedBy = reporter;
        this.createdAt = Instant.now();
    }

    void assignTo(Operator operator) {
        if (isSolved()) throw new DefectAlreadySolvedException();

        this.assignedTo = operator;
    }

    void solve(String solution) {
        if (isSolved()) throw new DefectAlreadySolvedException();

        this.solution = solution;
        this.solvedAt = Instant.now();
    }

    private boolean isSolved() {
        return solvedAt != null;
    }

    @Override
    public int compareTo(Defect other) {
        if (this.urgency == other.urgency) return this.createdAt.compareTo(other.createdAt);
        if (this.isSolved() && !other.isSolved()) return 1;
        if (!this.isSolved() && other.isSolved()) return -1;
        return compareUrgency(other);
    }

    private int compareUrgency(Defect other) {
        switch (this.urgency) {
            case LOW -> {
                return 1;
            }
            case MEDIUM -> {
                if (other.urgency == HIGH) return 1;
                return -1;
            }
            case HIGH -> {
                return -1;
            }
            default -> throw new UndefinedUrgencyException();
        }
    }

    @Override
    public String toString() {
        return "Defect{" +
            "urgency=" + urgency +
            ", description='" + description + '\'' +
            ", solution='" + solution + '\'' +
            ", createdAt=" + createdAt +
            ", solvedAt=" + solvedAt +
            ", assignedTo=" + assignedTo +
            ", reportedBy=" + reportedBy +
            '}';
    }
}
