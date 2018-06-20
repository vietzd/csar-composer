package org.opentosca.csarcomposer.population;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PopulationController {

    @Autowired
    private InternalCsarRestController internalCsarRestController;
    @Autowired
    private SourceCsarRestController sourceCsarRestController;

    @GetMapping("/")
    public ModelAndView getRepositories() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("sourceCsars", sourceCsarRestController.getSourceCsars());
        mv.addObject("internalCsars", internalCsarRestController.getInternalCsars());
        return mv;
    }
}