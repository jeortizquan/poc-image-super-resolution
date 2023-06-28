package eu.europeana.pocimagesuperresolution.thumbnail.domain.model;

import java.util.List;

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

    /**
     * Enhance list.
     *
     * @param inputFile the input file
     * @return the list
     */
    List<String> enhance(List<String> inputFile);
}
