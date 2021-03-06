package org.opentosca.csarcomposer.filter;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.winery.common.ids.definitions.RequirementTypeId;
import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;
import org.eclipse.winery.model.tosca.*;
import org.eclipse.winery.repository.backend.IRepository;
import org.eclipse.winery.repository.backend.RepositoryFactory;
import org.opentosca.csarcomposer.model.CSAR;
import org.opentosca.csarcomposer.model.Requirement;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FilterService {

    private List<CSAR> sourceRepository = new ArrayList<>();
    private Map<QName, Requirement> mapOfAllRequirements;
    private Predicate<? super CSAR> filter;

    public FilterService() {
        IRepository repo = RepositoryFactory.getRepository();

        SortedSet<ServiceTemplateId> allServiceTemplateIds = repo.getAllDefinitionsChildIds(ServiceTemplateId.class);
        for (ServiceTemplateId serviceTemplateId : allServiceTemplateIds) {
            TServiceTemplate tServiceTemplate = repo.getElement(serviceTemplateId);

            List<QName> resultCapabilities = listOfCapabilities(tServiceTemplate);
            List<Requirement> resultRequirements = listOfRequirements(tServiceTemplate);

            CSAR csar = new CSAR(serviceTemplateId, resultCapabilities, resultRequirements);
            System.out.println("Added " + csar);
            sourceRepository.add(csar);
        }
    }

    private Predicate<? super CSAR> createFilter(List<CSAR> internalRepository) {
        List<Requirement> usedRequirements = new ArrayList<>();
        List<QName> usedCapabilities = new ArrayList<>();
        internalRepository.forEach(internalCsar -> usedRequirements.addAll(internalCsar.getRequirements()));
        internalRepository.forEach(internalCsar -> usedCapabilities.addAll(internalCsar.getCapabilities()));

        return (Predicate<CSAR>) csar -> {

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

            if(usedCapabilities.isEmpty() && usedRequirements.isEmpty()) {
                return true;
            }

            return false;
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
            @NonNull TCapability ref = capabilityRef.getRef();
            TNodeTemplate referencedNodeTemplate = null;
            for (TNodeTemplate nodeTemplate : tServiceTemplate.getTopologyTemplate().getNodeTemplates()) {
                if (nodeTemplate != null && nodeTemplate.getCapabilities() != null) {
                    for (TCapability cap : nodeTemplate.getCapabilities().getCapability()) {
                        if (cap.equals(ref)) {
                            referencedNodeTemplate = nodeTemplate;
                        }
                    }
                }
            }
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
            @NonNull TRequirement ref = requirementRef.getRef();
            TNodeTemplate referencedNodeTemplate = null;
            for (TNodeTemplate nodeTemplate : tServiceTemplate.getTopologyTemplate().getNodeTemplates()) {
                if (nodeTemplate != null && nodeTemplate.getRequirements() != null) {
                    for (TRequirement req : nodeTemplate.getRequirements().getRequirement()) {
                        if (req.equals(ref)) {
                            referencedNodeTemplate = nodeTemplate;
                        }
                    }
                }
            }
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

    public List<CSAR> getAllSourceCsars() {
        return sourceRepository;
    }

    public List<CSAR> getCompatibleSourceCsars() {
        if (filter == null) {
            return sourceRepository;
        } else {
            return sourceRepository.stream().filter(this.filter).collect(Collectors.toList());
        }
    }

    public List<CSAR> getIncompatibleSourceCsars() {
        if (filter == null) {
            return null;
        } else {
            return sourceRepository.stream().filter(this.filter.negate()).collect(Collectors.toList());
        }
    }

    public void updateFilter(List<CSAR> internalRepository) {
        this.filter = createFilter(internalRepository);
    }

    public CSAR getSourceCsar(int i) {
        return sourceRepository.get(i);
    }

    public List<Requirement> getOpenRequirements(List<CSAR> internalRepository) {
        List<Requirement> result = new ArrayList<>();
        List<Requirement> usedRequirements = new ArrayList<>();
        List<QName> usedCapabilities = new ArrayList<>();
        internalRepository.forEach(internalCsar -> usedRequirements.addAll(internalCsar.getRequirements()));
        internalRepository.forEach(internalCsar -> usedCapabilities.addAll(internalCsar.getCapabilities()));
        for (Requirement req : usedRequirements) {
            if (!usedCapabilities.contains(req.getRequiredCapabilityType())) {
                result.add(req);
            }
        }
        return result;
    }
}
