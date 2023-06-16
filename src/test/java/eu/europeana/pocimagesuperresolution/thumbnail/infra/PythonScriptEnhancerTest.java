package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PythonScriptEnhancerTest {

    @ParameterizedTest
    @ValueSource(strings = {"RRDN(weights='gans')", "RDN(weights='psnr-large')", "RDN(weights='psnr-small')", "RDN(weights='noise-cancel')"})
    void givenPythonScript_whenPythonProcessInvokedAlternative_thenSuccess(String model) {
        // given
        final Path resourceDirectory = Paths.get("src", "test", "resources");
        final String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        final ResourceHandler resourceHandler = new ResourceHandler(absolutePath);
        final String testInputFile = resourceHandler.getResourceFile("img/lowres.jpg");

        // when
        PythonScriptEnhancer scriptEnhancer = new PythonScriptEnhancer(resourceHandler, model);
        final String testOutputFile = scriptEnhancer.enhance(testInputFile);

        // then
        assertTrue(Files.exists(Paths.get(testOutputFile)));
    }
}
