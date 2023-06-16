package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PythonGraalVMEnhancerTest {

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"RRDN(weights='gans')", "RDN(weights='psnr-large')", "RDN(weights='psnr-small')", "RDN(weights='noise-cancel')"})
    void givenPythonScript_whenNativePythonAlternative_thenSuccess(String model) {
        // given
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        final ResourceHandler resourceHandler = new ResourceHandler(absolutePath);
        final String testInputFile = resourceHandler.getResourceFile("img/other.jpg");

        // when
        PythonGraalVMEnhancer pythonGraalVMEnhancer = new PythonGraalVMEnhancer(resourceHandler, model);
        final String testOutputFile = pythonGraalVMEnhancer.enhance(testInputFile);

        // then
        assertTrue(Files.exists(Paths.get(testOutputFile)));
    }
}
