package org.opentosca.csarcomposer.population;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PopulationController {

    @Autowired
    PopulationService populationService;

    @GetMapping("/")
    public ModelAndView getRepositories() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("activeSourceCsars", populationService.getCompatibleSourceCsars());
        mv.addObject("inactiveSourceCsars", populationService.getIncompatibleSourceCsars());
        mv.addObject("internalCsars", populationService.getAllInternalCsars());
        return mv;
    }
}