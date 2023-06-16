package eu.europeana.pocimagesuperresolution.thumbnail.domain.service;

import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;

/**
 * The type Super resolution service.
 */
public class SuperResolutionService {

    private final ThumbnailEnhancer thumbnailEnhancer;

    /**
     * Instantiates a new Super resolution service.
     *
     * @param thumbnailEnhancer the thumbnail enhancer
     */
    public SuperResolutionService(ThumbnailEnhancer thumbnailEnhancer) {
        this.thumbnailEnhancer = thumbnailEnhancer;
    }

    /**
     * Enhance string.
     *
     * @param input the input
     * @return the string
     */
    public String enhance(String input) {
        // add pre-conditions
        final String outputUrl = thumbnailEnhancer.enhance(input);
        // add post-conditions
        return outputUrl;
    }
}
