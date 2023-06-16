package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * The type Python api enhancer.
 */
public class PythonApiEnhancer implements ThumbnailEnhancer {
    private final String apiURL;
    private final ResourceHandler resourceHandler;

    /**
     * Instantiates a new Python api enhancer.
     *
     * @param resourceHandler the resource handler
     * @param apiURL          the api url
     */
    public PythonApiEnhancer(ResourceHandler resourceHandler, String apiURL) {
        this.resourceHandler = resourceHandler;
        this.apiURL = apiURL;
    }

    @Override
    public String enhance(String inputFile) {
        final URI uri;
        try {
            uri = new URI(apiURL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        final String outputFile = resourceHandler.generateOutputFileName(inputFile);

        final JSONObject requestJson = new JSONObject();
        requestJson.put("input", inputFile);
        requestJson.put("output", outputFile);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> httpEntity = new HttpEntity<>(requestJson.toString(), headers);
        final RestTemplate restTemplate = new RestTemplate();
        final String response = restTemplate.postForObject(uri, httpEntity, String.class);
        final JSONObject jsonResponse = new JSONObject(response);

        if (jsonResponse.has("message") && jsonResponse.get("message").equals("successful")) {
            return outputFile;
        } else {
            return null;
        }

    }
}
