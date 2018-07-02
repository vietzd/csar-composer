package org.opentosca.csarcomposer.filter;

import org.opentosca.csarcomposer.model.Csar;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class FilterService {

    public Predicate<? super Csar> createFilter(List<Csar> internalRepository) {
        return csar -> !internalRepository.contains(csar);
    }
}
