package org.opentosca.csarcomposer.provisioning;

import org.opentosca.csarcomposer.population.CSAR;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ProvisioningService {

    void startProvisioning(List<CSAR> allInternalCsars) {
        System.out.println(allInternalCsars);
    }
}
