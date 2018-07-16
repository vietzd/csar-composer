package org.opentosca.csarcomposer.provisioning;

import org.opentosca.csarcomposer.model.Csar;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ProvisioningService {

    void startProvisioning(List<Csar> allInternalCsars) {


        // container client  opentoscacontainerlegacyapiclient.deployapplication


        // API Upload mit post csar

        // csar.csar.servicetemplate.id.buildplan.id die input - Parameter herausfinden

        // wenn man einen Post an csar.id.servicetemplate.id.buildplans.id.instances macht, wird der buildplan ausgeführt.
        // hier die Parameter mitgeben


        System.out.println(allInternalCsars);
    }
}
