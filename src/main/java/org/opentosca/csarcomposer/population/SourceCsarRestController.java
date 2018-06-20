package org.opentosca.csarcomposer.population;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SourceCsarRestController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/source-csars")
    public List<CSAR> getSourceCsars() {
        return populationService.getAllSourceCsars();
    }
}
