package org.opentosca.csarcomposer.sorting;

import org.eclipse.winery.common.ids.definitions.CapabilityTypeId;
import org.opentosca.csarcomposer.model.Csar;
import org.opentosca.csarcomposer.model.Requirement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TopologyHelper {

    private Map<Csar, List<Csar>> outgoingEdges = new HashMap<>();

    TopologyHelper(List<Csar> internalRepository) {
        for (Csar csar : internalRepository) {
            List<Csar> outgoingEdges = new ArrayList<>();
            List<CapabilityTypeId> caps = csar.getCapabilities();
            for (Csar possibleCandidate : internalRepository) {
                if (oneCapIsRequiredByCsar(caps, possibleCandidate)) {
                    outgoingEdges.add(possibleCandidate);
                }
            }
            this.outgoingEdges.put(csar, outgoingEdges);
        }
    }

    private boolean oneCapIsRequiredByCsar(List<CapabilityTypeId> caps, Csar possibleCandidate) {
        for (Requirement r : possibleCandidate.getRequirements()) {
            if (caps.contains(r.getRequiredCapabilityType())) {
                return true;
            }
        }
        return false;
    }

    List<Csar> getAllNodesWithNoIncomingEdges() {
        List<Csar> result = new ArrayList<>();
        for (Csar csar : outgoingEdges.keySet()) { // iterate over all CSARs
            if (hasNoIncomingEdges(csar)) {
                result.add(csar);
            }
        }
        return result;
    }

    boolean hasNoIncomingEdges(Csar csar) {
        for (List<Csar> nodes : outgoingEdges.values()) {
            if (nodes.contains(csar)) {
                return false;
            }
        }
        return true;
    }

    List<Csar> getOutgoingEdges(Csar csar) {
        return outgoingEdges.get(csar);
    }

    void removeEdge(Csar source, Csar target) {
        outgoingEdges.get(source).remove(target);
    }
}
