package org.opentosca.csarcomposer.sorting;

import org.opentosca.csarcomposer.model.Csar;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

@Service
public class SortingService {

    private TopologyHelper topologyHelper;

    public List<Csar> sort(List<Csar> internalRepository) {

        topologyHelper = new TopologyHelper(internalRepository);

        List<Csar> result = new ArrayList<>(kahn());

        if (!topologyHelper.getAllNodesWithOpenRequirements().isEmpty()) {
            System.out.println("Warning: There are open requirements or cycles left:");
            List<Csar> nodesWithOpenRequirements = topologyHelper.getAllNodesWithOpenRequirements();
            while(!nodesWithOpenRequirements.isEmpty()) {
                Csar someNode = nodesWithOpenRequirements.get(0);
                topologyHelper.removeAllRequirementsOf(someNode);
                result.addAll(kahn());
                nodesWithOpenRequirements = topologyHelper.getAllNodesWithOpenRequirements();
            }
        }

        return result;
    }

    private List<Csar> kahn() {
        List<Csar> result = new ArrayList<>();
        List<Csar> nodesWithNoRequirements = topologyHelper.getAllNodesWithNoRequirements();
        while (!nodesWithNoRequirements.isEmpty()) {
            Csar someNode = nodesWithNoRequirements.get(0);
            nodesWithNoRequirements.remove(0);
            result.add(topologyHelper.getOriginalNode(someNode));
            for (Csar from : topologyHelper.getAllNodesThatRequireSomeCapabilityOf(someNode)) {
                for (QName capability : from.getCapabilities()) {
                    topologyHelper.removeEdge(capability, someNode);
                }

                if (topologyHelper.hasNoIncomingEdges(from)) {
                    nodesWithNoRequirements.add(from);
                }
            }
        }
        return result;
    }
}
