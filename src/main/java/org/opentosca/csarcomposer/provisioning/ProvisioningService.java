package org.opentosca.csarcomposer.provisioning;

import org.opentosca.csarcomposer.model.Csar;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
class ProvisioningService {


    private CsarExportingService csarExportingService = new CsarExportingService();
    private CsarUploadingService csarUploadingService = new CsarUploadingService();
    private InstanceCreationService instanceCreationService = new InstanceCreationService();

    void startProvisioning(List<Csar> allInternalCsars) {
        List<File> csarFiles = csarExportingService.exportCsarsToFile(allInternalCsars);
        csarFiles.forEach(csarUploadingService::uploadCsar);
        allInternalCsars.forEach(instanceCreationService::createServiceInstance2);
    }
}
