package org.opentosca.csarcomposer.sorting;

import org.eclipse.winery.common.ids.definitions.CapabilityTypeId;
import org.opentosca.csarcomposer.population.CSAR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TopologyHelper {

    private Map<CSAR, List<CSAR>> outgoingEdges = new HashMap<>();

    TopologyHelper(List<CSAR> internalRepository) {
        for (CSAR csar : internalRepository) {
            List<CSAR> outgoingEdges = new ArrayList<>();
            List<CapabilityTypeId> caps = csar.getCapabilities();
            for (CSAR possibleCandidate : internalRepository) {
                if (oneCapIsRequiredByCsar(caps, possibleCandidate)) {
                    outgoingEdges.add(possibleCandidate);
                }
            }
            this.outgoingEdges.put(csar, outgoingEdges);
        }
    }

    private boolean oneCapIsRequiredByCsar(List<CapabilityTypeId> caps, CSAR possibleCandidate) {
        // TODO implement
        return false;
    }

    List<CSAR> getAllNodesWithNoIncomingEdges() {
        List<CSAR> result = new ArrayList<>();
        for (CSAR csar : outgoingEdges.keySet()) { // iterate over all CSARs
            if (hasNoIncomingEdges(csar)) {
                result.add(csar);
            }
        }
        return result;
    }

    boolean hasNoIncomingEdges(CSAR csar) {
        for (List<CSAR> nodes : outgoingEdges.values()) {
            if (nodes.contains(csar)) {
                return false;
            }
        }
        return true;
    }

    List<CSAR> getOutgoingEdges(CSAR csar) {
        return outgoingEdges.get(csar);
    }

    void removeEdge(CSAR source, CSAR target) {
        outgoingEdges.get(source).remove(target);
    }
}
