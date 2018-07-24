package org.opentosca.csarcomposer.provisioning;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.json.impl.provider.entity.JSONArrayProvider;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.opentosca.csarcomposer.model.Csar;

import javax.ws.rs.core.MediaType;

class InstanceCreationService {

    void createServiceInstance2(Csar csar) {
        String csarName = csar.getServiceTemplateId().getQName().getLocalPart();
        String mainServiceTemplateInstancesUrl = "http://localhost:1337/csars/" + csarName + ".csar/servicetemplates/" +
                "%257Bhttp%253A%252F%252Fopentosca.org%252Fservicetemplates%257D" + csarName + "/buildplans/" + csarName +
                "_buildPlan/instances";

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JSONArrayProvider.App.class);
        Client c = Client.create(cc);

        String inputParams = "[\n" +
                "  {\n" +
                "    \"name\": \"ApplicationPort\",\n" +
                "    \"type\": \"String\",\n" +
                "    \"required\": \"YES\",\n" +
                "    \"value\": \"9990\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"DockerEngineURL\",\n" +
                "    \"type\": \"String\",\n" +
                "    \"required\": \"YES\",\n" +
                "    \"value\": \"tcp://dind:2375\"\n" +
                "  }\n" +
                "]";

        JSONArray array = null;
        try {
            array = new JSONArray(inputParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        c.addFilter(new LoggingFilter());
        c.resource(mainServiceTemplateInstancesUrl).type(MediaType.APPLICATION_JSON_TYPE).post(array);
    }
}
