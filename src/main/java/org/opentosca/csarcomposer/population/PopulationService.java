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
    private List<CSAR> internalRepository = new ArrayList<>();

    List<CSAR> getAllSourceCsars() {
        List<CSAR> result = new ArrayList<>();
        SortedSet<ServiceTemplateId> allDefinitionsChildIds = sourceRepository.getAllDefinitionsChildIds(ServiceTemplateId.class);

        for (ServiceTemplateId serviceTemplateId : allDefinitionsChildIds) {
            result.add(new CSAR(serviceTemplateId));
        }

        return result;
    }

    List<CSAR> getAllInternalCsars() {
        return internalRepository;
    }

    void copyCsarFromSourceToInternal(CSAR csar) {
        //TODO
    }

    CSAR getCsar(ServiceTemplateId id) {
        // TODO
        return new CSAR(id);
    }
}
