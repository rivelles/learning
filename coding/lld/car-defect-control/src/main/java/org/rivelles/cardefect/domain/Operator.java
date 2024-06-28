package org.rivelles.cardefect.domain;

public class Operator extends User {
    protected Operator(String id, String name) {
        super(id, name);
    }

    public Defect report(DefectUrgency urgency, String description) {
        return new Defect(urgency, description, this);
    }

    public void solve(Defect defect, String solution) {
        defect.solve(solution);
    }
}
