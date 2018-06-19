package org.opentosca.csarcomposer.population;

import lombok.Data;
import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;

@Data
public class CSAR {
    private ServiceTemplateId serviceTemplateId;
    private String name;

    public CSAR(ServiceTemplateId serviceTemplateId) {
        this.serviceTemplateId = serviceTemplateId;
    }
}
