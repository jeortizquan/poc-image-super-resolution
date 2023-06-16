package eu.europeana.pocimagesuperresolution.thumbnail.application;

import eu.europeana.pocimagesuperresolution.thumbnail.domain.service.SuperResolutionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Super resolution application service.
 */
@Service
@Controller
@RestController
public class SuperResolutionApplicationService {
    private final SuperResolutionService superResolutionService;

    /**
     * Instantiates a new Super resolution application service.
     *
     * @param superResolutionService the super resolution service
     */
    public SuperResolutionApplicationService(SuperResolutionService superResolutionService) {
        this.superResolutionService = superResolutionService;
    }

    /**
     * Enhance thumbnail enhancer response.
     *
     * @param request the request
     * @return the thumbnail enhancer response
     */
    @PostMapping(value = "/enhance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ThumbnailEnhancerResponse enhance(@RequestBody ThumbnailEnhancerRequest request) {

        final String output = superResolutionService.enhance(request.getInput());

        return new ThumbnailEnhancerResponse(output);
    }

    /**
     * The type Thumbnail enhancer response.
     */
    public static class ThumbnailEnhancerResponse {
        /**
         * The Output.
         */
        final String output;

        /**
         * Instantiates a new Thumbnail enhancer response.
         *
         * @param response the response
         */
        public ThumbnailEnhancerResponse(String response) {
            this.output = response;
        }

        /**
         * Gets output.
         *
         * @return the output
         */
        public String getOutput() {
            return output;
        }
    }

    /**
     * The type Thumbnail enhancer request.
     */
    public static class ThumbnailEnhancerRequest {
        /**
         * The Output.
         */
        final String input;

        public ThumbnailEnhancerRequest() {
            //serializing issues
            this.input = "";
        }

        /**
         * Instantiates a new Thumbnail enhancer request.
         *
         * @param request the response
         */
        public ThumbnailEnhancerRequest(String request) {
            this.input = request;
        }

        /**
         * Gets input.
         *
         * @return the input
         */
        public String getInput() {
            return input;
        }
    }
}
