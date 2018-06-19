package org.opentosca.csarcomposer.population;

import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;

public class CSAR {
    private ServiceTemplateId serviceTemplateId;
    private String name;

    public CSAR(ServiceTemplateId serviceTemplateId) {
        this.serviceTemplateId = serviceTemplateId;
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
}
