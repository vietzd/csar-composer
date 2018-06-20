package org.opentosca.csarcomposer.population;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class InternalCsarRestController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/internalCsars")
    public List<CSAR> getInternalCsars() {
        return populationService.getAllInternalCsars();
    }

    @PostMapping("/internalCsars")
    public void addToInternal(HttpServletRequest request) {
        populationService.addToInternal(request.getParameter("csarId"));
    }

    @GetMapping("/internalCsars/{id}")
    public CSAR findInternalCsar(@PathVariable final String id) {
        return populationService.findInternalCsar(id);
    }

    @DeleteMapping(value = "/internalCsars/{id}")
    @ResponseBody
    public void remove(@PathVariable final String id) {
        populationService.removeFromInternal(id);
    }

    @PostMapping("/remove")
    public void remove(HttpServletRequest request) {
        populationService.removeFromInternal(request.getParameter("csarId"));
    }

}
