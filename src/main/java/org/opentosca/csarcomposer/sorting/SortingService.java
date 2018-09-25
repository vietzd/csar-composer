package org.opentosca.csarcomposer.sorting;

import org.opentosca.csarcomposer.model.Csar;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SortingService {

    private Graph graph;

    public List<Csar> sort(List<Csar> internalRepository) {

        graph = new Graph(internalRepository);

        List<Csar> result = new ArrayList<>(kahn());


        if (!graph.getAllNodesWithOpenRequirements().isEmpty()) {
            List<Csar> nodesWithOpenRequirements = graph.getAllNodesWithOpenRequirements();
            while (!nodesWithOpenRequirements.isEmpty()) {
                Csar someNode = nodesWithOpenRequirements.get(0);
                graph.removeAllRequirementsOf(someNode);
                result.addAll(kahn());
                nodesWithOpenRequirements = graph.getAllNodesWithOpenRequirements();
            }
        }

        return result;
    }

    private List<Csar> kahn() {
        List<Csar> result = new ArrayList<>();
        List<Csar> nodesWithNoRequirements = graph.getAllNodesWithNoRequirements();
        while (!nodesWithNoRequirements.isEmpty()) {
            Csar someNode = nodesWithNoRequirements.get(0);
            graph.removeNode(someNode);
            nodesWithNoRequirements.remove(0);
            result.add(graph.getOriginalNode(someNode));
            for (Csar edgeDestination : graph.getAllNodesThatRequireSomeCapabilityOf(someNode)) {
                graph.removeEdge(someNode, edgeDestination);
                if (graph.hasNoIncomingEdges(edgeDestination)) {
                    nodesWithNoRequirements.add(edgeDestination);
                }
            }
        }
        return result;
    }
}
