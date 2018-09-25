package org.opentosca.csarcomposer.provisioning;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.opentosca.csarcomposer.model.CSAR;
import org.opentosca.csarcomposer.model.Requirement;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
class ProvisioningService {

    private CsarExportingService csarExportingService = new CsarExportingService();
    private CsarUploadingService csarUploadingService = new CsarUploadingService();
    private InstanceCreationService instanceCreationService = new InstanceCreationService();
    private Map<QName, List<JSONArray>> availableParams = new HashMap<>();

    void startProvisioning(List<CSAR> allInternalCsars) {
        List<CSAR> onboardingCsars = generateOnboardingCsars(allInternalCsars);

        List<File> exportedCsars = csarExportingService.exportCsarsToFile(onboardingCsars);

        exportedCsars.forEach(csarUploadingService::uploadCsar);

        createInstancesFor(onboardingCsars);
    }

    private void createInstancesFor(List<CSAR> csars) {
        for (CSAR csar : csars) {
            instanceCreationService.createServiceInstance(csar, getRequiredParameter(csar));
            waitUntilCreated(csar);
            addOutputParameterToAvailableParams(csar);
        }
    }

    private void addOutputParameterToAvailableParams(CSAR csar) {
        for (QName capability : csar.getCapabilities()) {
            if (availableParams.get(capability) == null) {
                List<JSONArray> jsonArrayList = new ArrayList<>();
                availableParams.put(capability, jsonArrayList);
            }
            availableParams.get(capability).add(getOutputParameter(csar));
        }
    }

    private List<CSAR> generateOnboardingCsars(List<CSAR> allInternalCsars) {
        List<CSAR> result = new ArrayList<>();
        for (CSAR csar : allInternalCsars) {
            if (getInstancesOf(csar).length() == 0) {
                result.add(csar);
            } else {
                System.out.println("Found already running instance for " + csar.getServiceTemplateId());
                addOutputParameterToAvailableParams(csar);
            }
        }
        return result;
    }

    private JSONArray getInstancesOf(CSAR csar) {
        String csarName = csar.getServiceTemplateId().getQName().getLocalPart();
        String url = "http://localhost:1337/csars/" + csarName + ".csar/servicetemplates/" +
                "%257Bhttp%253A%252F%252Fopentosca.org%252Fservicetemplates%257D" + csarName + "/instances/";
        String responseEntity;
        try {
             responseEntity = getResponse(url);
        } catch (RuntimeException ex) {
            return new JSONArray();
        }
        try {
            JSONObject responseAsJson = new JSONObject(responseEntity);
            return responseAsJson.getJSONArray("service_template_instances");
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private List<JSONArray> getRequiredParameter(CSAR csar) {
        List<JSONArray> result = new ArrayList<>();
        for (Requirement requirement : csar.getRequirements()) {
            List<JSONArray> strings = availableParams.get(requirement.getRequiredCapabilityType());
            if (strings != null) {
                result.addAll(availableParams.get(requirement.getRequiredCapabilityType()));
            }
        }
        return result;
    }

    private void waitUntilCreated(CSAR csar) {
        int counter = 0;
        while (!getStatusOf(csar).equals("CREATED")) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (counter >= 120) {
                System.out.println("ERROR - Provisioning couldnt finish in one minute. Cancelling");
                return;
            }
            counter++;
        }
    }

    private String getStatusOf(CSAR csar) {
        String result = "";

        JSONArray instances = getInstancesOf(csar);
        if (instances.length() > 0) {
            try {
                // get status of last created instance
                result = instances.getJSONObject(instances.length() - 1).getString("state");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            result = "CREATING";
        }
        return result;
    }

    private JSONArray getOutputParameter(CSAR csar) {
        JSONArray result;
        String csarName = csar.getServiceTemplateId().getQName().getLocalPart();
        String url = "http://localhost:1337/csars/" + csarName + ".csar/servicetemplates/" +
                "%257Bhttp%253A%252F%252Fopentosca.org%252Fservicetemplates%257D" + csarName + "/buildplans/" + csarName +
                "_buildPlan/instances";
        String responseEntity = getResponse(url);

        try {
            JSONObject responseAsJson = new JSONObject(responseEntity);
            JSONArray planInstances = responseAsJson.getJSONArray("plan_instances");

            // TODO: for all instances
            result = planInstances.getJSONObject(0).getJSONArray("outputs");

        } catch (JSONException e) {
            e.printStackTrace();
            result = new JSONArray();
        }
        return result;
    }

    private String getResponse(String url) {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response.getEntity(String.class);
    }
}
