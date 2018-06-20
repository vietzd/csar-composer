package org.opentosca.csarcomposer.population;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class InternalCsarRestController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/internal-csars")
    public List<CSAR> getInternalCsars() {
        return populationService.getAllInternalCsars();
    }

    @PostMapping("/internal-csars/add-from-source/{sourceId}")
    public void addToInternal(@PathVariable final String sourceId) {
        populationService.addToInternal(sourceId);
    }

    @GetMapping("/internal-csars/{id}")
    public CSAR findInternalCsar(@PathVariable final String id) {
        return populationService.findInternalCsar(id);
    }

    @DeleteMapping(value = "/internal-csars/{id}")
    public void remove(@PathVariable final String id) {
        populationService.removeFromInternal(id);
    }
}
