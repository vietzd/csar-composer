package org.opentosca.csarcomposer.sorting;

import org.opentosca.csarcomposer.model.CSAR;
import org.opentosca.csarcomposer.model.Requirement;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collectors;

class Graph {

    private List<CSAR> originalRepository;
    private List<CSAR> internalRepository = new ArrayList<>();

    Graph(List<CSAR> originalRepository) {
        this.originalRepository = originalRepository;
        originalRepository.forEach(csar -> internalRepository.add(new CSAR(csar)));
    }

    void ignoreOpenCapabilities() {
        for (CSAR csar : internalRepository) {
            for (QName capability : csar.getCapabilities()) {
                boolean capabilityIsUsed = false;
                for (CSAR possibleCandidate : internalRepository) {
                    List<Requirement> requirements = possibleCandidate.getRequirements();
                    for (Requirement requirement : requirements) {
                        if (requirement.getRequiredCapabilityType().equals(capability)) {
                            capabilityIsUsed = true;
                        }
                    }
                }
                if (!capabilityIsUsed) {
                    csar.getCapabilities().remove(capability);
                }
            }
        }
    }

    List<CSAR> getAllNodesWithNoRequirements() {
        return internalRepository.stream().filter(this::hasNoIncomingEdges).collect(Collectors.toList());
    }

    boolean hasNoIncomingEdges(CSAR csar) {
        return csar.getRequirements().isEmpty();
    }

    List<CSAR> getAllNodesWithOpenRequirements() {
        return internalRepository.stream().filter(this::hasOpenRequirements).collect(Collectors.toList());
    }

    private boolean hasOpenRequirements(CSAR csar) {
        return  csar.getRequirements() != null && !csar.getRequirements().isEmpty();
    }

    List<CSAR> getAllNodesThatRequireSomeCapabilityOf(CSAR someNode) {
        List<CSAR> result = new ArrayList<>();

        List<QName> capabilities = someNode.getCapabilities();

        for (CSAR possibleCandidate : internalRepository) {
            List<Requirement> requirements = possibleCandidate.getRequirements();
            for (Requirement r : requirements) {
                if (capabilities.contains(r.getRequiredCapabilityType())) {
                    result.add(possibleCandidate);
                }
            }
        }

        return result;
    }

    /**
     * Removes all requirements of someNode with given capability as requiredCapability
     * @param capability if this capabilityType is required, the particular requirement will be deleted
     * @param to some of its requirements will be removed
     */
    void removeEdge(CSAR someNode, CSAR to) {
        for (QName capability : someNode.getCapabilities()) {
            removeEdge(capability, to);
        }
    }

    private void removeEdge(QName capability, CSAR someNode){
        List<Requirement> requirementsToRemove = new ArrayList<>();
        for (Requirement r : someNode.getRequirements()) {
            if (capability.equals(r.getRequiredCapabilityType())) {
                requirementsToRemove.add(r);
            }
        }
        someNode.getRequirements().removeAll(requirementsToRemove);
    }

    CSAR getOriginalNode(CSAR someNode) {
        for (CSAR csar : originalRepository) {
            if (csar.getId() == someNode.getId()) {
                return csar;
            }
        }
        System.out.println("Error: didn't find original csar for " + someNode);
        return someNode;
    }

    void removeAllRequirementsOf(CSAR someNode) {
        someNode.getRequirements().clear();
    }

    public boolean hasNodes() {
        return !internalRepository.isEmpty();
    }

    public void removeNode(CSAR someNode) {
        internalRepository.remove(someNode);
    }
}
