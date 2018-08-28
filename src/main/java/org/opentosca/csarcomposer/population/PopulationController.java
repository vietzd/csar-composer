package org.opentosca.csarcomposer.population;

import org.opentosca.csarcomposer.model.Csar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PopulationController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/api/internal-csars")
    public List<Csar> getInternalCsars() {
        return populationService.getAllInternalCsars();
    }

    @PostMapping("/api/internal-csars/add-from-source/{sourceId}")
    public void addToInternal(@PathVariable final String sourceId) {
        populationService.addToInternal(sourceId);
    }

    @GetMapping("/api/internal-csars/{id}")
    public Csar findInternalCsar(@PathVariable final String id) {
        return populationService.findInternalCsar(id);
    }

    @DeleteMapping("/api/internal-csars/{id}")
    public void remove(@PathVariable final String id) {
        populationService.removeFromInternal(id);
    }
}
