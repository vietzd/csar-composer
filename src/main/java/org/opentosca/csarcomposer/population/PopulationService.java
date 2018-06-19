package org.opentosca.csarcomposer.population;

import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;
import org.eclipse.winery.repository.backend.IRepository;
import org.eclipse.winery.repository.backend.RepositoryFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@Service
public class PopulationService {


    private IRepository sourceRepository = RepositoryFactory.getRepository();

    public List<CSAR> getAllServiceTemplateIds() {
        List<CSAR> result = new ArrayList<>();
        SortedSet<ServiceTemplateId> allDefinitionsChildIds = sourceRepository.getAllDefinitionsChildIds(ServiceTemplateId.class);

        for (ServiceTemplateId serviceTemplateId : allDefinitionsChildIds) {
            result.add(new CSAR(serviceTemplateId));
        }

        return result;
    }

    public void addServiceTemplateTo(CSAR csar) {
        adjustFilter();
    }

    public void removeCSAR(CSAR csar) {
        adjustFilter();
    }

    private void adjustFilter() {

    }
}
