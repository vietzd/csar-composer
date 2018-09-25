package org.opentosca.csarcomposer;

import org.opentosca.csarcomposer.filter.FilterService;
import org.opentosca.csarcomposer.population.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    PopulationService populationService;
    @Autowired
    FilterService filterService;

    @GetMapping("/")
    public ModelAndView getRepositories() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("activeSourceCsars", filterService.getCompatibleSourceCsars());
        mv.addObject("inactiveSourceCsars", filterService.getIncompatibleSourceCsars());
        mv.addObject("internalCsars", populationService.getAllInternalCsars());
        mv.addObject("openRequirements", filterService.getOpenRequirements(populationService.getAllInternalCsars()));
        return mv;
    }
}