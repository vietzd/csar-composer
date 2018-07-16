package org.opentosca.csarcomposer.population;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.winery.common.ids.definitions.RequirementTypeId;
import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;
import org.eclipse.winery.model.tosca.*;
import org.eclipse.winery.repository.backend.IRepository;
import org.eclipse.winery.repository.backend.RepositoryFactory;
import org.opentosca.csarcomposer.filter.FilterService;
import org.opentosca.csarcomposer.model.Csar;
import org.opentosca.csarcomposer.model.Requirement;
import org.opentosca.csarcomposer.sorting.SortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PopulationService {

    @Autowired
    SortingService sortingService;
    @Autowired
    FilterService filterService;

    private List<Csar> sourceRepository = new ArrayList<>();
    private List<Csar> internalRepository = new ArrayList<>();
    private Predicate<? super Csar> filter;
    private Map<QName, Requirement> mapOfAllRequirements;

    public PopulationService() {
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

    private List<QName> listOfCapabilities(TServiceTemplate tServiceTemplate) {
        List<QName> result = new ArrayList<>();

        TBoundaryDefinitions.@Nullable Capabilities capabilities = tServiceTemplate.getBoundaryDefinitions().getCapabilities();
        if (capabilities == null) {
            return result;
        }

        @NonNull List<TCapabilityRef> capabilityRefs = capabilities.getCapability();
        for (TCapabilityRef capabilityRef : capabilityRefs) {
            TNodeTemplate referencedNodeTemplate = (TNodeTemplate) capabilityRef.getRef();
            TNodeTemplate.Capabilities capabilitiesOfReferencedNode = referencedNodeTemplate.getCapabilities();
            if(capabilitiesOfReferencedNode != null) {
                @NonNull List<TCapability> capability = capabilitiesOfReferencedNode.getCapability();
                for (TCapability c : capability) {
                    result.add(c.getType());
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
            TNodeTemplate.Requirements requirementsOfReferencedNode = referencedNodeTemplate.getRequirements();
            if (requirementsOfReferencedNode != null) {
                @NonNull List<TRequirement> requirement = requirementsOfReferencedNode.getRequirement();
                for (TRequirement r : requirement) {
                    @NonNull QName type = r.getType();
                    result.add(hashmap.get(type));
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

    public List<Csar> getAllInternalCsars() {
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


    public Csar findInternalCsar(String id) {
        // TODO
        return internalRepository.get(0);
    }
}
