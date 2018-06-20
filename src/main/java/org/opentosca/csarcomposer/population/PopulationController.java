package org.opentosca.csarcomposer.population;

import org.eclipse.winery.common.ids.definitions.ServiceTemplateId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @GetMapping("/sourceCsars")
    public List<CSAR> getSourceCsars(Model m) {
        return populationService.getAllSourceCsars();
    }

    @GetMapping("/internalCsars")
    public List<CSAR> getInternalCsars(Model m) {
        return populationService.getAllInternalCsars();
    }

    @PostMapping("/add")
    public String copyCsarFromSourceToInternal(HttpServletRequest request) {
        System.out.println("Add Csar: " + request.getParameter("csarId"));
        populationService.copyCsarFromSourceToInternal(request.getParameter("csarId"));
        return "redirect:/";
    }

    @RequestMapping("/sourceCsars/{id}")
    public CSAR getCsar(@PathVariable ServiceTemplateId id) {
        return populationService.getCsar(id);
    }
}