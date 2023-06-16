package eu.europeana.pocimagesuperresolution.thumbnail.configuration;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;
import eu.europeana.pocimagesuperresolution.thumbnail.domain.service.SuperResolutionService;
import eu.europeana.pocimagesuperresolution.thumbnail.infra.PythonScriptEnhancer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Poc config.
 */
@Configuration
public class PocConfig {
    private static final String MODEL = "RDN(weights='noise-cancel')";


    /**
     * Resource handler
     *
     * @param resourcePath the resource path
     * @return the resource handler
     */
    @Bean
    ResourceHandler resourceHandler(@Value("${resources.directory}") String resourcePath) {
        return new ResourceHandler(resourcePath);
    }

    /**
     * Thumbnail enhancer
     *
     * @param resourceHandler the resource handler
     * @return the thumbnail enhancer
     */
    @Bean
    ThumbnailEnhancer thumbnailEnhancer(ResourceHandler resourceHandler) {
        return new PythonScriptEnhancer(resourceHandler, MODEL);
    }

    /**
     * Super resolution service
     *
     * @param thumbnailEnhancer the thumbnail enhancer
     * @return the super resolution service
     */
    @Bean
    SuperResolutionService superResolutionService(ThumbnailEnhancer thumbnailEnhancer) {
        return new SuperResolutionService(thumbnailEnhancer);
    }
}
