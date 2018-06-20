package org.opentosca.csarcomposer.api;

import org.opentosca.csarcomposer.population.CSAR;
import org.opentosca.csarcomposer.population.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SourceCsarRestController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/api/source-csars")
    public List<CSAR> getSourceCsars() {
        return populationService.getAllSourceCsars();
    }
}
