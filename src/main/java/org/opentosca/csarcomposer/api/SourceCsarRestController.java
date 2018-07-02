package org.opentosca.csarcomposer.api;

import org.opentosca.csarcomposer.model.Csar;
import org.opentosca.csarcomposer.population.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SourceCsarRestController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/api/source-csars")
    public List<Csar> getAllSourceCsars() {
        return populationService.getAllSourceCsars();
    }

    @GetMapping("/api/compatible-source-csars")
    public List<Csar> getCompatibleSourceCsars() {
        return populationService.getCompatibleSourceCsars();
    }

    @GetMapping("/api/incompatible-source-csars")
    public List<Csar> getIncompatibleSourceCsars() {
        return populationService.getIncompatibleSourceCsars();
    }
}
