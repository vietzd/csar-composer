package org.opentosca.csarcomposer.filter;

import org.opentosca.csarcomposer.population.CSAR;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class FilterService {

    public Predicate<? super CSAR> createFilter(List<CSAR> internalRepository) {
        return csar -> !internalRepository.contains(csar);
    }
}
