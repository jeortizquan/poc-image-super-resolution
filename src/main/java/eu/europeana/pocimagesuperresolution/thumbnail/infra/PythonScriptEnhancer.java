package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * The type Python script enhancer.
 */
public class PythonScriptEnhancer implements ThumbnailEnhancer {

    private final String model;
    private final ResourceHandler resourceHandler;

    /**
     * Instantiates a new Python script enhancer.
     *
     * @param model the model
     */
    public PythonScriptEnhancer(ResourceHandler resourceHandler, String model) {
        this.resourceHandler = resourceHandler;
        this.model = model;
    }

    @Override
    public String enhance(String inputFile) {
        final String outputFile = resourceHandler.generateOutputFileName(inputFile);
        final String pythonScript = resourceHandler.getResourceFileContent("scripts/isr_lib.py")
                .replace("{:0}", inputFile)
                .replace("{:1}", outputFile);
        File tempFile = resourceHandler.createPythonTempFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {

            fileOutputStream.write(pythonScript.replace("{:2}", model).getBytes(StandardCharsets.UTF_8));
            final String path = tempFile.getAbsolutePath();
            // Improvements: implement a command executor for this
            ProcessBuilder processBuilder = new ProcessBuilder("python3", path);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            final List<String> results = resourceHandler.readProcessOutput(process.getInputStream());
            results.stream().forEach(System.out::println);
            final int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException(String.format("Script execution failed exit code %d", exitCode));
            }
            return outputFile;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

