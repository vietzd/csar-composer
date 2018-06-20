package org.opentosca.csarcomposer.population;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
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

    @PostMapping("/add")
    public String add(HttpServletRequest request) {
        System.out.println("Add Csar: " + request.getParameter("csarId"));
        populationService.addToInternal(request.getParameter("csarId"));
        return "redirect:/";
    }

    @PostMapping("/remove")
    public String remove(HttpServletRequest request) {
        System.out.println("Add Csar: " + request.getParameter("csarId"));
        populationService.removeFromInternal(request.getParameter("csarId"));
        return "redirect:/";
    }
}