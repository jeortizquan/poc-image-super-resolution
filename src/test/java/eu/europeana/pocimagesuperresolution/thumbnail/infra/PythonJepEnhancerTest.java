package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PythonJepEnhancerTest {

    @Test
    void givenPythonScript_whenPythonLibraryInvoked_thenSuccess() {
        final String model = "RDN(weights='noise-cancel')";
        // given
        final Path resourceDirectory = Paths.get("src", "test", "resources");
        final String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        final ResourceHandler resourceHandler = new ResourceHandler(absolutePath);
        final String testInputFile = resourceHandler.getResourceFile("img/piggies.jpeg");

        // when
        PythonJepEnhancer jepEnhancer = new PythonJepEnhancer(resourceHandler, model);
        final String testOutputFile = jepEnhancer.enhance(testInputFile);

        // then
        assertTrue(Files.exists(Paths.get(testOutputFile)));
    }
}
