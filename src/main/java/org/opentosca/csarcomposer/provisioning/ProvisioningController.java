package org.opentosca.csarcomposer.provisioning;

import org.opentosca.csarcomposer.population.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProvisioningController {

    @Autowired
    PopulationService populationService;
    @Autowired
    ProvisioningService provisioningService;

    @GetMapping("/start-provisioning")
    public ModelAndView getRepositories() {
        ModelAndView mv = new ModelAndView("provisioning");
        mv.addObject("internalCsars", populationService.getAllInternalCsars());
        provisioningService.startProvisioning(populationService.getAllInternalCsars());
        return mv;
    }
}
