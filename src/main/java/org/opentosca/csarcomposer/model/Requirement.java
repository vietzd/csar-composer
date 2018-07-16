package org.opentosca.csarcomposer.model;

import javax.xml.namespace.QName;

public class Requirement {

    private QName requirementType;
    private QName requiredCapabilityType;

    public Requirement(QName requirementQName, QName requiredCapabilityType) {
        this.requirementType = requirementQName;
        this.requiredCapabilityType = requiredCapabilityType;
    }


    public QName getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(QName requirementType) {
        this.requirementType = requirementType;
    }

    public QName getRequiredCapabilityType() {
        return requiredCapabilityType;
    }

    public void setRequiredCapabilityType(QName requiredCapabilityType) {
        this.requiredCapabilityType = requiredCapabilityType;
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "requirementType=" + requirementType +
                ", requiredCapabilityType=" + requiredCapabilityType +
                '}';
    }
}
