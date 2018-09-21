package org.opentosca.csarcomposer.sorting;

import com.sun.xml.bind.v2.TODO;
import org.opentosca.csarcomposer.model.Csar;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

@Service
public class SortingService {

    private Graph graph;

    public List<Csar> sort(List<Csar> internalRepository) {

        graph = new Graph(internalRepository);

        List<Csar> result = null;
        try {
            result = new ArrayList<>(kahn());
        } catch (CyclicGraphException e) {
            e.printStackTrace();
        }

        if (!graph.getAllNodesWithOpenRequirements().isEmpty()) {
            List<Csar> nodesWithOpenRequirements = graph.getAllNodesWithOpenRequirements();
            while(!nodesWithOpenRequirements.isEmpty()) {
                Csar someNode = nodesWithOpenRequirements.get(0);
                graph.removeAllRequirementsOf(someNode);
                try {
                    result.addAll(kahn());
                } catch (CyclicGraphException e) {
                    e.printStackTrace();
                }
                nodesWithOpenRequirements = graph.getAllNodesWithOpenRequirements();
            }
        }

        return result;
    }

    private List<Csar> kahn() throws CyclicGraphException{
        List<Csar> result = new ArrayList<>();
        List<Csar> nodesWithNoRequirements = graph.getAllNodesWithNoRequirements();
        while (!nodesWithNoRequirements.isEmpty()) {
            Csar someNode = nodesWithNoRequirements.get(0);
            graph.removeNode(someNode);
            nodesWithNoRequirements.remove(0);
            result.add(graph.getOriginalNode(someNode));
            for (Csar from : graph.getAllNodesThatRequireSomeCapabilityOf(someNode)) {
                graph.removeEdge(from, someNode);
                if (graph.hasNoIncomingEdges(from)) {
                    nodesWithNoRequirements.add(from);
                }
            }
        }
        if (graph.hasNodes()) {
            throw new CyclicGraphException();
        }
        return result;
    }
}
