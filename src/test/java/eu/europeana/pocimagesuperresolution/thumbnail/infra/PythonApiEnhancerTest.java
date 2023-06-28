package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        final String testInputFile = resourceHandler.getResourceFile("img/piggies.jpeg");
        PythonApiEnhancer pythonApiEnhancer = new PythonApiEnhancer(resourceHandler,"http://localhost:5050");

        // when
        final String testOutputFile = pythonApiEnhancer.enhance(testInputFile);

        // then
        assertTrue(Files.exists(Paths.get(testOutputFile)));
    }

    @Test
    void givenImageBatch_whenPythonApiServerInvoked_thenSuccess() {
        final String model = "RDN(weights='noise-cancel')";
        // given
        final Path resourceDirectory = Paths.get("src", "test", "resources");
        final String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        final ResourceHandler resourceHandler = new ResourceHandler(absolutePath);
        final String testInputFile1 = resourceHandler.getResourceFile("img/among.jpg");
        final String testInputFile2 = resourceHandler.getResourceFile("img/face.jpg");
        final String testInputFile3 = resourceHandler.getResourceFile("img/lowres.jpg");
        final String testInputFile4 = resourceHandler.getResourceFile("img/other.jpg");
        final String testInputFile5 = resourceHandler.getResourceFile("img/piggies.jpeg");
        // when
        PythonApiEnhancer pythonApiEnhancer = new PythonApiEnhancer(resourceHandler,"http://localhost:5050");
        final List<String> testOutputFiles = pythonApiEnhancer.enhance(List.of(testInputFile1,
                testInputFile2, testInputFile3, testInputFile4, testInputFile5));

        // then
        testOutputFiles.forEach(testOutputFile-> {assertTrue(Files.exists(Paths.get(testOutputFile)));});
    }
}
