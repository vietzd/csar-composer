package org.opentosca.csarcomposer.model;

import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;

import javax.xml.namespace.QName;
import java.util.List;

public class Csar {

    private static int ID = 0;

    private int id;
    private ServiceTemplateId serviceTemplateId;
    private List<QName> capabilities;
    private List<Requirement> requirements;

    public Csar(ServiceTemplateId serviceTemplateId, List<QName> capabilities, List<Requirement> requirements) {
        this.id = ID++;
        this.serviceTemplateId = serviceTemplateId;
        this.capabilities = capabilities;
        this.requirements = requirements;
    }

    public List<QName> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<QName> capabilities) {
        this.capabilities = capabilities;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
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

    @Override
    public String toString() {
        return "Csar{" +
                "id=" + id +
                ", capabilities=" + capabilities +
                ", requirements=" + requirements +
                '}';
    }
}
