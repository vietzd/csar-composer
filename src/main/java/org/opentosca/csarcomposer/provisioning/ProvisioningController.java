package org.opentosca.csarcomposer.provisioning;

import org.opentosca.csarcomposer.population.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProvisioningController {

    @Autowired
    PopulationService populationService;
    @Autowired
    ProvisioningService provisioningService;

    @GetMapping("/start-provisioning")
    //TODO POST Mapping!
    public ModelAndView getRepositories() {
        ModelAndView mv = new ModelAndView("provisioning");
        mv.addObject("internalCsars", populationService.getAllInternalCsars());
        provisioningService.startProvisioning(populationService.getAllInternalCsars());
        return mv;
    }

//    @GetMapping("/api/internal-csars/{sourceId}/status")
//    public String getStatus(@PathVariable final String sourceId) {
//        return provisioningService.getStatusOf(sourceId);
//    }
}
