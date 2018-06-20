package org.opentosca.csarcomposer.population;

import org.opentosca.csarcomposer.api.InternalCsarRestController;
import org.opentosca.csarcomposer.api.SourceCsarRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PopulationController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/")
    public ModelAndView getRepositories() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("sourceCsars", populationService.getAllSourceCsars());
        mv.addObject("internalCsars", populationService.getAllInternalCsars());
        return mv;
    }
}