package org.opentosca.csarcomposer.sorting;

import org.opentosca.csarcomposer.model.Csar;
import org.opentosca.csarcomposer.model.Requirement;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collectors;

class Graph {

    private List<Csar> originalRepository;
    private List<Csar> internalRepository = new ArrayList<>();

    Graph(List<Csar> originalRepository) {
        this.originalRepository = originalRepository;
        originalRepository.forEach(csar -> internalRepository.add(new Csar(csar)));
    }

    void ignoreOpenCapabilities() {
        for (Csar csar : internalRepository) {
            for (QName capability : csar.getCapabilities()) {
                boolean capabilityIsUsed = false;
                for (Csar possibleCandidate : internalRepository) {
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

    List<Csar> getAllNodesWithNoRequirements() {
        return internalRepository.stream().filter(this::hasNoIncomingEdges).collect(Collectors.toList());
    }

    boolean hasNoIncomingEdges(Csar csar) {
        return csar.getRequirements().isEmpty();
    }

    List<Csar> getAllNodesWithOpenRequirements() {
        return internalRepository.stream().filter(this::hasOpenRequirements).collect(Collectors.toList());
    }

    private boolean hasOpenRequirements(Csar csar) {
        return  csar.getRequirements() != null && !csar.getRequirements().isEmpty();
    }

    List<Csar> getAllNodesThatRequireSomeCapabilityOf(Csar someNode) {
        List<Csar> result = new ArrayList<>();

        List<QName> capabilities = someNode.getCapabilities();

        for (Csar possibleCandidate : internalRepository) {
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
    void removeEdge(Csar someNode, Csar to) {
        for (QName capability : someNode.getCapabilities()) {
            removeEdge(capability, to);
        }
    }

    private void removeEdge(QName capability, Csar someNode){
        List<Requirement> requirementsToRemove = new ArrayList<>();
        for (Requirement r : someNode.getRequirements()) {
            if (capability.equals(r.getRequiredCapabilityType())) {
                requirementsToRemove.add(r);
            }
        }
        someNode.getRequirements().removeAll(requirementsToRemove);
    }

    Csar getOriginalNode(Csar someNode) {
        for (Csar csar : originalRepository) {
            if (csar.getId() == someNode.getId()) {
                return csar;
            }
        }
        System.out.println("Error: didn't find original csar for " + someNode);
        return someNode;
    }

    void removeAllRequirementsOf(Csar someNode) {
        someNode.getRequirements().clear();
    }

    public boolean hasNodes() {
        return !internalRepository.isEmpty();
    }

    public void removeNode(Csar someNode) {
        internalRepository.remove(someNode);
    }
}
