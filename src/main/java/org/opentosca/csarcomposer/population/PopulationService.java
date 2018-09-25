package org.opentosca.csarcomposer.population;

import org.opentosca.csarcomposer.filter.FilterService;
import org.opentosca.csarcomposer.model.CSAR;
import org.opentosca.csarcomposer.sorting.SortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PopulationService {

    @Autowired
    SortingService sortingService;
    @Autowired
    FilterService filterService;

    private List<CSAR> internalRepository = new ArrayList<>();


    public List<CSAR> getAllInternalCsars() {
        return internalRepository;
    }

    public void addToInternal(String indexOfSourceCsar) {
        internalRepository.add(filterService.getSourceCsar(Integer.parseInt(indexOfSourceCsar)));
        internalRepository = sortingService.sort(internalRepository);
        filterService.updateFilter(internalRepository);
    }

    public void removeFromInternal(String csar) {
        internalRepository.remove(filterService.getSourceCsar(Integer.parseInt(csar)));
        internalRepository = sortingService.sort(internalRepository);
        filterService.updateFilter(internalRepository);
    }

    public CSAR findInternalCsar(String id) {
        // TODO
        return internalRepository.get(0);
    }
}
