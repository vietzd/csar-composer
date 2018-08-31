package org.opentosca.csarcomposer.provisioning;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.opentosca.csarcomposer.model.Csar;
import org.opentosca.csarcomposer.model.Requirement;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
class ProvisioningService {

    private CsarExportingService csarExportingService = new CsarExportingService();
    private CsarUploadingService csarUploadingService = new CsarUploadingService();
    private InstanceCreationService instanceCreationService = new InstanceCreationService();
    private Map<QName, List<JSONArray>> availableParams = new HashMap<>();

    void startProvisioning(List<Csar> allInternalCsars) {
        List<File> csarFiles = csarExportingService.exportCsarsToFile(allInternalCsars);
        csarFiles.forEach(csarUploadingService::uploadCsar);
        for (Csar csar : allInternalCsars) {
            instanceCreationService.createServiceInstance(csar, getRequiredParameter(csar));
            waitUntilCreated(csar);
            for (QName capability : csar.getCapabilities()) {
                // initialize array if capability is not in availableParams yet
                if (availableParams.get(capability) == null) {
                    List<JSONArray> jsonArrayList = new ArrayList<>();
                    availableParams.put(capability, jsonArrayList);
                }
                availableParams.get(capability).add(getOutputParameter(csar));
            }
        }
    }

    private List<JSONArray> getRequiredParameter(Csar csar) {
        List<JSONArray> result = new ArrayList<>();
        for (Requirement requirement : csar.getRequirements()) {
            List<JSONArray> strings = availableParams.get(requirement.getRequiredCapabilityType());
            if (strings != null) {
                result.addAll(availableParams.get(requirement.getRequiredCapabilityType()));
            }
        }
        return result;
    }

    private void waitUntilCreated(Csar csar) {
        int counter = 0;
        while (!getStatusOf(csar).equals("CREATED")) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (counter >= 10) {
                System.out.println("ERROR - Provisioning couldnt finish in one minute. Cancelling");
                return;
            }
            counter++;
        }
    }

    private String getStatusOf(Csar csar) {
        String result;

        String csarName = csar.getServiceTemplateId().getQName().getLocalPart();
        String mainServiceTemplateInstancesUrl = "http://localhost:1337/csars/" + csarName + ".csar/servicetemplates/" +
                "%257Bhttp%253A%252F%252Fopentosca.org%252Fservicetemplates%257D" + csarName + "/instances/";

        Client client = Client.create();
        WebResource webResource = client.resource(mainServiceTemplateInstancesUrl);
        ClientResponse response = webResource.get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String responseEntity = response.getEntity(String.class);

        try {
            JSONObject responseAsJson = new JSONObject(responseEntity);
            JSONArray instances = responseAsJson.getJSONArray("service_template_instances");
            result = instances.getJSONObject(instances.length()-1).getString("state"); // Status of last created instance
        } catch (JSONException e) {
            e.printStackTrace();
            result = "CREATED";
        }

        return result;
    }

    private JSONArray getOutputParameter(Csar csar) {
        JSONArray result;

        String csarName = csar.getServiceTemplateId().getQName().getLocalPart();
        String mainServiceTemplateInstancesUrl = "http://localhost:1337/csars/" + csarName + ".csar/servicetemplates/" +
                "%257Bhttp%253A%252F%252Fopentosca.org%252Fservicetemplates%257D" + csarName + "/buildplans/" + csarName +
                "_buildPlan/instances";

        Client client = Client.create();
        WebResource webResource = client.resource(mainServiceTemplateInstancesUrl);
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        String responseEntity = response.getEntity(String.class);

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
}
