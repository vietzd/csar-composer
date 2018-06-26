package org.opentosca.csarcomposer.population;

import org.eclipse.winery.common.ids.definitions.CapabilityTypeId;
import org.eclipse.winery.common.ids.definitions.RequirementTypeId;
import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;

import java.util.ArrayList;
import java.util.List;

public class CSAR {

    private static int ID = 0;

    private int id;
    private ServiceTemplateId serviceTemplateId;
    private String name;
    private List<CapabilityTypeId> capabilities = new ArrayList<>();
    private List<RequirementTypeId> requirements = new ArrayList<>();

    public List<CapabilityTypeId> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilityTypeId> capabilities) {
        this.capabilities = capabilities;
    }

    public List<RequirementTypeId> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<RequirementTypeId> requirements) {
        this.requirements = requirements;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServiceTemplateId getServiceTemplateId() {
        return serviceTemplateId;
    }

    public void setServiceTemplateId(ServiceTemplateId serviceTemplateId) {
        this.serviceTemplateId = serviceTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CSAR(ServiceTemplateId serviceTemplateId) {
        this.id = ID++;
        this.serviceTemplateId = serviceTemplateId;
    }
}
