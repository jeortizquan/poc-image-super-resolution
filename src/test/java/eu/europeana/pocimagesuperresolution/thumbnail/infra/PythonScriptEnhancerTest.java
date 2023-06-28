package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PythonScriptEnhancerTest {

    @Test
    void givenPythonScript_whenPythonProcessInvokedAlternative_thenSuccess() {
        final String model = "RDN(weights='noise-cancel')";
        // given
        final Path resourceDirectory = Paths.get("src", "test", "resources");
        final String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        final ResourceHandler resourceHandler = new ResourceHandler(absolutePath);
        final String testInputFile = resourceHandler.getResourceFile("img/piggies.jpeg");

        // when
        PythonScriptEnhancer scriptEnhancer = new PythonScriptEnhancer(resourceHandler, model);
        final String testOutputFile = scriptEnhancer.enhance(testInputFile);

        // then
        assertTrue(Files.exists(Paths.get(testOutputFile)));
    }

    @Test
    void givenPythonScript_whenPythonBatchProcessInvokedAlternative_thenSuccess() {
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
        PythonScriptEnhancer scriptEnhancer = new PythonScriptEnhancer(resourceHandler, model);
        final List<String> testOutputFiles = scriptEnhancer.enhance(List.of(testInputFile1,
                testInputFile2, testInputFile3, testInputFile4, testInputFile5));

        // then
        testOutputFiles.forEach(testOutputFile-> {assertTrue(Files.exists(Paths.get(testOutputFile)));});

    }
}
