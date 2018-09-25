package org.opentosca.csarcomposer.provisioning;

import org.eclipse.winery.repository.backend.RepositoryFactory;
import org.eclipse.winery.repository.exceptions.RepositoryCorruptException;
import org.eclipse.winery.repository.export.CsarExporter;
import org.jvnet.hk2.annotations.Service;
import org.opentosca.csarcomposer.model.CSAR;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
class CsarExportingService {

    List<File> exportCsarsToFile(List<CSAR> csars) {
        System.out.println("Export CSARs: " + csars);
        try {
            return exportCsarsToFileEx(csars);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<File> exportCsarsToFileEx(List<CSAR> csars) throws IOException {
        List<File> result = new ArrayList<>();
        for (CSAR csar : csars) {

            CsarExporter csarExporter = new CsarExporter();

            String path = System.getProperty("user.home") + "/export-test/" + csar.getServiceTemplateId().getQName().getLocalPart() + ".csar";
            File file = new File(path);

            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    continue;
                }
            }

            OutputStream outputStream = new FileOutputStream(file);
            try {
                String s = csarExporter.writeCsar(RepositoryFactory.getRepository(), csar.getServiceTemplateId(), outputStream, new HashMap<>());
            } catch (JAXBException | RepositoryCorruptException e) {
                e.printStackTrace();
            }

            outputStream.close();

            result.add(file);
            System.out.println("Created: " + file);
        }
        return result;
    }
}
