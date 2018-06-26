package org.opentosca.csarcomposer.population;

import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;
import org.eclipse.winery.model.tosca.TRequirementRef;
import org.eclipse.winery.model.tosca.TServiceTemplate;
import org.eclipse.winery.repository.backend.IRepository;
import org.eclipse.winery.repository.backend.RepositoryFactory;
import org.opentosca.csarcomposer.sorting.SortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@Service
public class PopulationService {

    @Autowired
    SortingService sortingService;

    private List<CSAR> sourceRepository = new ArrayList<>();
    private List<CSAR> internalRepository = new ArrayList<>();

    public PopulationService() {
        IRepository repo = RepositoryFactory.getRepository();
        SortedSet<ServiceTemplateId> allDefinitionsChildIds = repo.getAllDefinitionsChildIds(ServiceTemplateId.class);

        for (ServiceTemplateId serviceTemplateId : allDefinitionsChildIds) {
            sourceRepository.add(new CSAR(serviceTemplateId));
        }
    }

    public List<CSAR> getAllSourceCsars() {
        return sourceRepository;
    }

    public List<CSAR> getAllInternalCsars() {
        return internalRepository;
    }

    public void addToInternal(String indexOfSourceCsar) {
        internalRepository.add(sourceRepository.get(Integer.parseInt(indexOfSourceCsar)));
        internalRepository = sortingService.sort(internalRepository);
    }

    public void removeFromInternal(String csar) {
        internalRepository.remove(sourceRepository.get(Integer.parseInt(csar)));
        internalRepository = sortingService.sort(internalRepository);
    }

    public void addToInternal(CSAR c) {
        internalRepository.add(c);
    }

    public CSAR findInternalCsar(String id) {

        return internalRepository.get(0);
    }
}
