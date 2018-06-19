package org.opentosca.csarcomposer.population;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class PopulationController {

    @Autowired
    private PopulationService populationService;

    @GetMapping("/")
    public ModelAndView getRepositories() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("sourceCsars", populationService.getAllSourceCsars());
        mv.addObject("internalCsars", populationService.getAllInternalCsars());
        return mv;
    }

    @GetMapping("/sourceCsars")
    public List<CSAR> getSourceCsars(Model m) {
        return populationService.getAllSourceCsars();
    }

    @GetMapping("/internalCsars")
    public List<CSAR> getInternalCsars(Model m) {
        return populationService.getAllInternalCsars();
    }
}