package org.opentosca.csarcomposer.provisioning;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.json.impl.provider.entity.JSONArrayProvider;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.MediaType;
import java.io.File;

@Service
class CsarUploadingService {

    void uploadCsar(File csarFile) {
        String url = "http://localhost:1337/csars";
        String pathToCsar = csarFile.getAbsolutePath();
        if (!csarFile.exists() || !csarFile.isFile()) {
            System.out.println("Upload not possible as the CSAR file at " + pathToCsar + " couldn't be found");
            return;
        }
        System.out.println("Uploading " + pathToCsar);

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(MultiPartWriter.class);
        cc.getClasses().add(JSONArrayProvider.App.class);
        Client client = Client.create(cc);
        WebResource webResource = client.resource(url);

        // the file to upload, represented as FileDataBodyPart
        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", csarFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        @SuppressWarnings("resource") final MultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(fileDataBodyPart);
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        WebResource.Builder builder = webResource.type(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON);
        ClientResponse response = builder.post(ClientResponse.class, multiPart);
        response.close();
        System.out.println(response.getStatus());
    }
}
