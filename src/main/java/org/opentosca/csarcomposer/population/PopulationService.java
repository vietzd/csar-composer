package org.opentosca.csarcomposer.population;

import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;
import org.eclipse.winery.model.tosca.TRequirementRef;
import org.eclipse.winery.model.tosca.TServiceTemplate;
import org.eclipse.winery.repository.backend.IRepository;
import org.eclipse.winery.repository.backend.RepositoryFactory;
import org.opentosca.csarcomposer.filter.FilterService;
import org.opentosca.csarcomposer.sorting.SortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PopulationService {

    @Autowired
    SortingService sortingService;
    @Autowired
    FilterService filterService;

    private List<CSAR> sourceRepository = new ArrayList<>();
    private List<CSAR> internalRepository = new ArrayList<>();
    private Predicate<? super CSAR> filter;

    public PopulationService() {
        IRepository repo = RepositoryFactory.getRepository();
        SortedSet<ServiceTemplateId> allDefinitionsChildIds = repo.getAllDefinitionsChildIds(ServiceTemplateId.class);
//        TServiceTemplate element = repo.getElement(allDefinitionsChildIds.first());
//        List<TRequirementRef> requirement = element.getBoundaryDefinitions().getRequirements().getRequirement();

        for (ServiceTemplateId serviceTemplateId : allDefinitionsChildIds) {
            sourceRepository.add(new CSAR(serviceTemplateId));
        }
    }

    public List<CSAR> getAllSourceCsars() {
        if (filter == null) {
            return sourceRepository;
        } else {
            return sourceRepository.stream().filter(this.filter).collect(Collectors.toList());
        }
    }

    public List<CSAR> getAllInternalCsars() {
        return internalRepository;
    }

    public void addToInternal(String indexOfSourceCsar) {
        internalRepository.add(sourceRepository.get(Integer.parseInt(indexOfSourceCsar)));
        internalRepository = sortingService.sort(internalRepository);
        filter = filterService.createFilter(internalRepository);
    }

    public void removeFromInternal(String csar) {
        internalRepository.remove(sourceRepository.get(Integer.parseInt(csar)));
        internalRepository = sortingService.sort(internalRepository);
        filter = filterService.createFilter(internalRepository);
    }


    public CSAR findInternalCsar(String id) {
        // TODO
        return internalRepository.get(0);
    }
}
