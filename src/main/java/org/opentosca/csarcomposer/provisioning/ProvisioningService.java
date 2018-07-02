package org.opentosca.csarcomposer.provisioning;

import org.opentosca.csarcomposer.model.Csar;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ProvisioningService {

    void startProvisioning(List<Csar> allInternalCsars) {
        System.out.println(allInternalCsars);
    }
}
