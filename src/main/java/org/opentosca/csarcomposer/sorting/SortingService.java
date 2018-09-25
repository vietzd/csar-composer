package org.opentosca.csarcomposer.sorting;

import org.opentosca.csarcomposer.model.CSAR;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SortingService {

    private Graph graph;

    public List<CSAR> sort(List<CSAR> internalRepository) {

        graph = new Graph(internalRepository);

        List<CSAR> result = new ArrayList<>(kahn());


        if (!graph.getAllNodesWithOpenRequirements().isEmpty()) {
            List<CSAR> nodesWithOpenRequirements = graph.getAllNodesWithOpenRequirements();
            while (!nodesWithOpenRequirements.isEmpty()) {
                CSAR someNode = nodesWithOpenRequirements.get(0);
                graph.removeAllRequirementsOf(someNode);
                result.addAll(kahn());
                nodesWithOpenRequirements = graph.getAllNodesWithOpenRequirements();
            }
        }

        return result;
    }

    private List<CSAR> kahn() {
        List<CSAR> result = new ArrayList<>();
        List<CSAR> nodesWithNoRequirements = graph.getAllNodesWithNoRequirements();
        while (!nodesWithNoRequirements.isEmpty()) {
            CSAR someNode = nodesWithNoRequirements.get(0);
            graph.removeNode(someNode);
            nodesWithNoRequirements.remove(0);
            result.add(graph.getOriginalNode(someNode));
            for (CSAR edgeDestination : graph.getAllNodesThatRequireSomeCapabilityOf(someNode)) {
                graph.removeEdge(someNode, edgeDestination);
                if (graph.hasNoIncomingEdges(edgeDestination)) {
                    nodesWithNoRequirements.add(edgeDestination);
                }
            }
        }
        return result;
    }
}
