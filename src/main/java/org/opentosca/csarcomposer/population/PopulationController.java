package org.opentosca.csarcomposer.population;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PopulationController {

    @Autowired
    private PopulationService populationService;

    @GetMapping("/")
    public ModelAndView getRepositories() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("sourceServiceTemplateIds", populationService.getAllServiceTemplateIds());
        return mv;
    }




    @RequestMapping(method = RequestMethod.POST, value = "/repos")
    public void addTopic(@RequestBody CSAR csar) {
        System.out.println(csar.getName());
    }

}