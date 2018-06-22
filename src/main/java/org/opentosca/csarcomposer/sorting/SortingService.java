package org.opentosca.csarcomposer.sorting;

import org.eclipse.winery.common.ids.definitions.CapabilityTypeId;
import org.eclipse.winery.common.ids.definitions.RequirementTypeId;
import org.opentosca.csarcomposer.population.CSAR;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SortingService {

    private int[][] graph;
    private List<CSAR> internalRepository;

    public List<CSAR> sort(List<CSAR> internalRepository) {
        this.internalRepository = internalRepository;
        createGraph();
//        return(kahnsAlgorithm());
        return internalRepository;
    }

    private void createGraph() {
        int numberOfCsars = internalRepository.size();

        graph = new int[numberOfCsars][numberOfCsars];

        for (CSAR csar : internalRepository) {
            List<CapabilityTypeId> capabilities = csar.getCapabilities();
            for (CapabilityTypeId cap : capabilities) {
                List<CSAR> dependingCsars = findAllCsarsThatRequire(cap);
                for (CSAR dependingCsar : dependingCsars) {
                    addDependencyToGraph(csar, dependingCsar);
                }
            }
        }
    }

    private void addDependencyToGraph(CSAR csar, CSAR dependingCsar) {
        int indexOfCsar = internalRepository.indexOf(csar);
        int indexOfDependingCsar = internalRepository.indexOf(dependingCsar);
        graph[indexOfCsar][indexOfDependingCsar]++;
    }

    private List<CSAR> findAllCsarsThatRequire(CapabilityTypeId cap) {
        List<CSAR> result = new ArrayList<>();
        for (CSAR csar : internalRepository) {
            for (RequirementTypeId req : csar.getRequirements()) {
                // TODO is compareto correct?
                if (cap.compareTo(req) == 0) {
                    result.add(csar);
                }
            }
        }
        return result;
    }

    private List<CSAR> kahnsAlgorithm() {
        List<CSAR> result = new ArrayList<CSAR>();
        List<CSAR> nodesWithNoIncomingEdges = generateListWithNoIncomingEdges();

        while (!nodesWithNoIncomingEdges.isEmpty()) {
            CSAR node = nodesWithNoIncomingEdges.get(0);
            nodesWithNoIncomingEdges.remove(0);
            result.add(node);
            // TODO:
            // for each node m with an edge e from node to m do
                // remove edge e from the graph
                // if m has no other incoming edges then
                    // insert m into nodesWithNoIncomingEdges
        }

        // TODO: if graph has edges: return error

        return result;
    }

    private List<CSAR> generateListWithNoIncomingEdges() {
        // TODO
        return null;
    }

}
