package org.opentosca.csarcomposer.api;

import org.opentosca.csarcomposer.population.CSAR;
import org.opentosca.csarcomposer.population.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class InternalCsarRestController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/api/internal-csars")
    public List<CSAR> getInternalCsars() {
        return populationService.getAllInternalCsars();
    }

    @PostMapping("/api/internal-csars/add-from-source/{sourceId}")
    public void addToInternal(@PathVariable final String sourceId) {
        populationService.addToInternal(sourceId);
    }

    @GetMapping("/api/internal-csars/{id}")
    public CSAR findInternalCsar(@PathVariable final String id) {
        return populationService.findInternalCsar(id);
    }

    @DeleteMapping("/api/internal-csars/{id}")
    public void remove(@PathVariable final String id) {
        populationService.removeFromInternal(id);
    }
}
