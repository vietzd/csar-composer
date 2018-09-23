package org.opentosca.csarcomposer.filter;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.winery.common.ids.definitions.RequirementTypeId;
import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;
import org.eclipse.winery.model.tosca.*;
import org.eclipse.winery.repository.backend.IRepository;
import org.eclipse.winery.repository.backend.RepositoryFactory;
import org.opentosca.csarcomposer.model.Csar;
import org.opentosca.csarcomposer.model.Requirement;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FilterService {

    private List<Csar> sourceRepository = new ArrayList<>();
    private Map<QName, Requirement> mapOfAllRequirements;
    private Predicate<? super Csar> filter;

    public FilterService() {
        IRepository repo = RepositoryFactory.getRepository();

        SortedSet<ServiceTemplateId> allServiceTemplateIds = repo.getAllDefinitionsChildIds(ServiceTemplateId.class);
        for (ServiceTemplateId serviceTemplateId : allServiceTemplateIds) {
            TServiceTemplate tServiceTemplate = repo.getElement(serviceTemplateId);

            List<QName> resultCapabilities = listOfCapabilities(tServiceTemplate);
            List<Requirement> resultRequirements = listOfRequirements(tServiceTemplate);

            Csar csar = new Csar(serviceTemplateId, resultCapabilities, resultRequirements);
            System.out.println("Added " + csar);
            sourceRepository.add(csar);
        }
    }

    private Predicate<? super Csar> createFilter(List<Csar> internalRepository) {
        List<Requirement> usedRequirements = new ArrayList<>();
        List<QName> usedCapabilities = new ArrayList<>();
        internalRepository.forEach(internalCsar -> usedRequirements.addAll(internalCsar.getRequirements()));
        internalRepository.forEach(internalCsar -> usedCapabilities.addAll(internalCsar.getCapabilities()));

        return (Predicate<Csar>) csar -> {

            // Don't allow any capability twice
            for (QName usedCapability : usedCapabilities) {
                if (csar.getCapabilities().contains(usedCapability)) {
                    return false;
                }
            }

            // Allow all capabilities that fulfill any active requirement
            for (Requirement usedRequirement : usedRequirements) {
                QName requiredCapability = usedRequirement.getRequiredCapabilityType();
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

    private List<QName> listOfCapabilities(TServiceTemplate tServiceTemplate) {
        List<QName> result = new ArrayList<>();

        TBoundaryDefinitions.@Nullable Capabilities capabilities = tServiceTemplate.getBoundaryDefinitions().getCapabilities();
        if (capabilities == null) {
            return result;
        }

        @NonNull List<TCapabilityRef> capabilityRefs = capabilities.getCapability();
        for (TCapabilityRef capabilityRef : capabilityRefs) {
            TNodeTemplate referencedNodeTemplate = (TNodeTemplate) capabilityRef.getRef();
            if (referencedNodeTemplate != null) {
                TNodeTemplate.Capabilities capabilitiesOfReferencedNode = referencedNodeTemplate.getCapabilities();
                if (capabilitiesOfReferencedNode != null) {
                    @NonNull List<TCapability> capability = capabilitiesOfReferencedNode.getCapability();
                    for (TCapability c : capability) {
                        result.add(c.getType());
                    }
                }
            }
        }

        return result;
    }

    private List<Requirement> listOfRequirements(TServiceTemplate tServiceTemplate) {
        List<Requirement> result = new ArrayList<>();

        Map<QName, Requirement> hashmap = mapOfAllRequirements();
        TBoundaryDefinitions.@Nullable Requirements boundaryRequirements = tServiceTemplate.getBoundaryDefinitions().getRequirements();
        if (boundaryRequirements == null) {
            return result;
        }

        @NonNull List<TRequirementRef> requirementRefs = boundaryRequirements.getRequirement();
        for (TRequirementRef requirementRef : requirementRefs) {
            TNodeTemplate referencedNodeTemplate = (TNodeTemplate) requirementRef.getRef();
            if (referencedNodeTemplate != null) {
                TNodeTemplate.Requirements requirementsOfReferencedNode = referencedNodeTemplate.getRequirements();
                if (requirementsOfReferencedNode != null) {
                    @NonNull List<TRequirement> requirement = requirementsOfReferencedNode.getRequirement();
                    for (TRequirement r : requirement) {
                        @NonNull QName type = r.getType();
                        result.add(hashmap.get(type));
                    }
                }
            }
        }
        return result;
    }

    private Map<QName, Requirement> mapOfAllRequirements() {
        if (mapOfAllRequirements == null) {
            mapOfAllRequirements = new HashMap<>();
            IRepository repo = RepositoryFactory.getRepository();
            SortedSet<RequirementTypeId> allRequirementTypeIds = repo.getAllDefinitionsChildIds(RequirementTypeId.class);
            for (RequirementTypeId requirementTypeId : allRequirementTypeIds) {
                TRequirementType req = repo.getElement(requirementTypeId);
                QName requirementQName = requirementTypeId.getQName();
                QName requiredCapabilityType = req.getRequiredCapabilityType();
                Requirement requirement = new Requirement(requirementQName, requiredCapabilityType);
                mapOfAllRequirements.put(requirementQName, requirement);
            }
        }

        return mapOfAllRequirements;
    }

    public List<Csar> getAllSourceCsars() {
        return sourceRepository;
    }

    public List<Csar> getCompatibleSourceCsars() {
        if (filter == null) {
            return sourceRepository;
        } else {
            return sourceRepository.stream().filter(this.filter).collect(Collectors.toList());
        }
    }

    public List<Csar> getIncompatibleSourceCsars() {
        if (filter == null) {
            return null;
        } else {
            return sourceRepository.stream().filter(this.filter.negate()).collect(Collectors.toList());
        }
    }

    public void updateFilter(List<Csar> internalRepository) {
        this.filter = createFilter(internalRepository);
    }

    public Csar getSourceCsar(int i) {
        return sourceRepository.get(i);
    }
}
