package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class PythonApiEnhancerTest {

    @Test
    void givenImage_whenPythonApiServerInvoked_thenSuccess() {
        // This needs the following context:
        // python3 installed + ISR library
        // An ISR localserver running on port 5050
        // https://github.com/europeana/rd-img-superresolution/blob/main/model-api/api.py
        // command:
        //  python3 api.py

        // given
        final Path resourceDirectory = Paths.get("src", "test", "resources");
        final String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        final ResourceHandler resourceHandler = new ResourceHandler(absolutePath);
        final String testInputFile = resourceHandler.getResourceFile("img/lowres.jpg");
        PythonApiEnhancer pythonApiEnhancer = new PythonApiEnhancer(resourceHandler,"http://localhost:5050/srapi");

        // when
        final String testOutputFile = pythonApiEnhancer.enhance(testInputFile);

        // then
        assertTrue(Files.exists(Paths.get(testOutputFile)));
    }
}
