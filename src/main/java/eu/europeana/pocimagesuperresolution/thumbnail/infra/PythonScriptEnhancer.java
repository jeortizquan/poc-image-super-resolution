package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    @Override
    public List<String> enhance(List<String> inputFileList) {
        List<String> outputFileList = new ArrayList<>();
        for(String inputFile : inputFileList) {
            outputFileList.add(resourceHandler.generateOutputFileName(inputFile));
        }
        final String pythonScript = resourceHandler.getResourceFile("scripts/isr_batch.py");
        try {
            // Improvements: implement a command executor for this
            ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScript,
                    "--input1", inputFileList.get(0), "--output1", outputFileList.get(0),
                    "--input2", inputFileList.get(1), "--output2", outputFileList.get(1),
                    "--input3", inputFileList.get(2), "--output3", outputFileList.get(2),
                    "--input4", inputFileList.get(3), "--output4", outputFileList.get(3),
                    "--input5", inputFileList.get(4), "--output5", outputFileList.get(4));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            final List<String> results = resourceHandler.readProcessOutput(process.getInputStream());
            results.stream().forEach(System.out::println);
            final int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException(String.format("Script execution failed exit code %d", exitCode));
            }
            return outputFileList;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

