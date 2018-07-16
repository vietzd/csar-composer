package org.opentosca.csarcomposer.filter;

import org.eclipse.winery.common.ids.definitions.CapabilityTypeId;
import org.opentosca.csarcomposer.model.Csar;
import org.opentosca.csarcomposer.model.Requirement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class FilterService {

    public Predicate<? super Csar> createFilter(List<Csar> internalRepository) {
        List<Requirement> usedRequirements = new ArrayList<>();
        List<CapabilityTypeId> usedCapabilities = new ArrayList<>();
        internalRepository.forEach(internalCsar -> usedRequirements.addAll(internalCsar.getRequirements()));
        internalRepository.forEach(internalCsar -> usedCapabilities.addAll(internalCsar.getCapabilities()));

        return (Predicate<Csar>) csar -> {

            // Don't allow any capability twice
            for (CapabilityTypeId usedCapability : usedCapabilities) {
                if (csar.getCapabilities().contains(usedCapability)) {
                    return false;
                }
            }

            // Allow all capabilities that fulfill any active requirement
            for (Requirement usedRequirement : usedRequirements) {
                CapabilityTypeId requiredCapability = usedRequirement.getRequiredCapabilityType();
                if (csar.getCapabilities().contains(requiredCapability)) {
                    return true;
                }
            }

            // Allow all requirements that use any active capability
            for (Requirement requirement : csar.getRequirements()) {
                if (usedCapabilities.contains(requirement.getRequiredCapabilityType())) {
                    return true;
                }
            }

            return true; // TODO return false
        };
    }
}
