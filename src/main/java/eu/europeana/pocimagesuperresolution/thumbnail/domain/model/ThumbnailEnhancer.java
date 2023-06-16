package eu.europeana.pocimagesuperresolution.thumbnail.domain.model;

/**
 * The interface Thumbnail enhancer.
 */
public interface ThumbnailEnhancer {
    /**
     * Enhance string.
     *
     * @param input the input
     * @return the string
     */
    String enhance(String input);
}
