package org.opentosca.csarcomposer.population;

import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;

public class CSAR {

    private static int ID = 0;

    private int id;
    private ServiceTemplateId serviceTemplateId;
    private String name;

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
