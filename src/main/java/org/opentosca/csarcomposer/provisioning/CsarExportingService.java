package org.opentosca.csarcomposer.provisioning;

import org.apache.commons.compress.archivers.ArchiveException;
import org.eclipse.winery.repository.backend.RepositoryFactory;
import org.eclipse.winery.repository.exceptions.RepositoryCorruptException;
import org.eclipse.winery.repository.export.CsarExporter;
import org.jvnet.hk2.annotations.Service;
import org.opentosca.csarcomposer.model.Csar;

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

    List<File> exportCsarsToFile(List<Csar> csars) {
        System.out.println("Export CSARs: " + csars);
        try {
            return exportCsarsToFileException(csars);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<File> exportCsarsToFileException(List<Csar> csars) throws IOException {
        List<File> result = new ArrayList<>();
        for (Csar csar : csars) {

            CsarExporter csarExporter = new CsarExporter();

            String path = "/home/daniel/export-test/" + csar.getServiceTemplateId().getQName().getLocalPart() + ".csar";
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
            } catch (ArchiveException | JAXBException | RepositoryCorruptException e) {
                e.printStackTrace();
            }

            outputStream.close();

            result.add(file);
            System.out.println("Created: " + file);
        }
        return result;
    }
}
