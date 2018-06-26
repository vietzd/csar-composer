package org.opentosca.csarcomposer.sorting;

import org.opentosca.csarcomposer.population.CSAR;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SortingService {

    public List<CSAR> sort(List<CSAR> internalRepository) {
        TopologyHelper topologyHelper = new TopologyHelper(internalRepository);

        List<CSAR> result = new ArrayList<>();
        List<CSAR> nodesWithNoIncomingEdges = topologyHelper.getAllNodesWithNoIncomingEdges();

        while (!nodesWithNoIncomingEdges.isEmpty()) {
            CSAR someNode = nodesWithNoIncomingEdges.get(0);
            nodesWithNoIncomingEdges.remove(0);
            result.add(someNode);
            for (CSAR from : topologyHelper.getOutgoingEdges(someNode)) {
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
