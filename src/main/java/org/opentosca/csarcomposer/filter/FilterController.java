package org.opentosca.csarcomposer.filter;

import org.opentosca.csarcomposer.model.CSAR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FilterController {

    @Autowired
    FilterService filterService;

    @GetMapping("/api/source-csars")
    public List<CSAR> getAllSourceCsars() {
        return filterService.getAllSourceCsars();
    }

    @GetMapping("/api/compatible-source-csars")
    public List<CSAR> getCompatibleSourceCsars() {
        return filterService.getCompatibleSourceCsars();
    }

    @GetMapping("/api/incompatible-source-csars")
    public List<CSAR> getIncompatibleSourceCsars() {
        return filterService.getIncompatibleSourceCsars();
    }
}
