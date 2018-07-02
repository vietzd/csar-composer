package org.opentosca.csarcomposer.sorting;

import org.opentosca.csarcomposer.model.Csar;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SortingService {

    public List<Csar> sort(List<Csar> internalRepository) {
        TopologyHelper topologyHelper = new TopologyHelper(internalRepository);

        List<Csar> result = new ArrayList<>();
        List<Csar> nodesWithNoIncomingEdges = topologyHelper.getAllNodesWithNoIncomingEdges();

        while (!nodesWithNoIncomingEdges.isEmpty()) {
            Csar someNode = nodesWithNoIncomingEdges.get(0);
            nodesWithNoIncomingEdges.remove(0);
            result.add(someNode);
            for (Csar from : topologyHelper.getOutgoingEdges(someNode)) {
                topologyHelper.removeEdge(from, someNode);

                if (topologyHelper.hasNoIncomingEdges(from)) {
                    nodesWithNoIncomingEdges.add(from);
                }
            }
        }
        // TODO: if graph has edges: return error (cycles)

        return result;
    }
}
