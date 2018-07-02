package org.opentosca.csarcomposer.model;

import org.eclipse.winery.common.ids.definitions.CapabilityTypeId;
import org.eclipse.winery.common.ids.definitions.RequirementTypeId;

public class Requirement {

    private RequirementTypeId requirementTypeId;
    private CapabilityTypeId requiredCapabilityType;


    public RequirementTypeId getRequirementTypeId() {
        return requirementTypeId;
    }

    public void setRequirementTypeId(RequirementTypeId requirementTypeId) {
        this.requirementTypeId = requirementTypeId;
    }

    public CapabilityTypeId getRequiredCapabilityType() {
        return requiredCapabilityType;
    }

    public void setRequiredCapabilityType(CapabilityTypeId requiredCapabilityType) {
        this.requiredCapabilityType = requiredCapabilityType;
    }
}
