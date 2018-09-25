package org.opentosca.csarcomposer.provisioning;


import org.opentosca.csarcomposer.model.CSAR;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

class InstanceDecommissioningService {
    void decommissionService(CSAR csar, int instanceId) {
        String csarName = csar.getServiceTemplateId().getQName().getLocalPart();
        String mainServiceTemplateInstancesUrl = "http://localhost:1337/csars/" + csarName + ".csar/servicetemplates/" +
                "%257Bhttp%253A%252F%252Fopentosca.org%252Fservicetemplates%257D" + csarName + "/instances/" + instanceId;

        Client client = ClientBuilder.newClient();
        WebTarget OrderByIdTarget = client.target(mainServiceTemplateInstancesUrl);
        boolean response = OrderByIdTarget.request().delete(Boolean.class);
        System.out.println("Deletion successful: " + response);
    }
}
