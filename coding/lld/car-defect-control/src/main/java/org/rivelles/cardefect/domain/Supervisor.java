package org.rivelles.cardefect.domain;

public class Supervisor extends User {
    protected Supervisor(String id, String name) {
        super(id, name);
    }

    public void reassign(Defect defect, Operator operator) {
        defect.assignTo(operator);
    }
}
