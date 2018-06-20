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



    private List<CSAR> sourceRepository = new ArrayList<>();
    private List<CSAR> internalRepository = new ArrayList<>();

    public PopulationService() {
        IRepository repo = RepositoryFactory.getRepository();
        SortedSet<ServiceTemplateId> allDefinitionsChildIds = repo.getAllDefinitionsChildIds(ServiceTemplateId.class);

        for (ServiceTemplateId serviceTemplateId : allDefinitionsChildIds) {
            sourceRepository.add(new CSAR(serviceTemplateId));
        }
    }

    List<CSAR> getAllSourceCsars() {
        return sourceRepository;
    }

    List<CSAR> getAllInternalCsars() {
        return internalRepository;
    }

    void copyCsarFromSourceToInternal(String csar) {
        internalRepository.add(sourceRepository.get(Integer.parseInt(csar)));
    }

    CSAR getCsar(ServiceTemplateId id) {
        // TODO
        return new CSAR(id);
    }
}
