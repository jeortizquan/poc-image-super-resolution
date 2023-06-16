package eu.europeana.pocimagesuperresolution.thumbnail.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Resource handler.
 */
public class ResourceHandler {

    /**
     * The constant OUTPUT_FILE_NAME_FORMATTER.
     */
    public static final DateTimeFormatter OUTPUT_FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss");
    private final String resourceScriptPath;

    /**
     * Instantiates a new Resource handler.
     *
     * @param resourceScriptPath the resource script path
     */
    public ResourceHandler(String resourceScriptPath) {
        this.resourceScriptPath = resourceScriptPath;
    }

    /**
     * Create python temp file.
     *
     * @return the file
     */
    public File createPythonTempFile() {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("pyt", ".py");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }

    /**
     * Read process output list.
     *
     * @param inputStream the input stream
     * @return the list
     * @throws IOException the io exception
     */
    public List<String> readProcessOutput(InputStream inputStream) throws IOException {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringList.add(line);
            }
        }
        return stringList;
    }

    /**
     * Gets resource file.
     *
     * @param fileName the file name
     * @return the resource file
     */
    public String getResourceFile(String fileName) {
        return this.resourceScriptPath.concat("/").concat(fileName);
    }

    /**
     * Gets extension by string handling.
     *
     * @param filename the filename
     * @return the extension by string handling
     */
    public String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse("img");
    }

    /**
     * Generate output file name string.
     *
     * @param inputFile the input file
     * @return the string
     */
    public String generateOutputFileName(String inputFile) {
        final Path outputFileHandler = Paths.get(inputFile);
        final String outputFileExtension = "." + getExtensionByStringHandling(outputFileHandler.getFileName().toString());
        final String inputFileName = outputFileHandler.getFileName().toString();
        return inputFile.replace(inputFileName, inputFileName.replace(outputFileExtension, "") +
                LocalDateTime.now().format(ResourceHandler.OUTPUT_FILE_NAME_FORMATTER) + outputFileExtension);
    }

    /**
     * Gets resource file content.
     *
     * @param fileName the file name
     * @return the resource file content
     */
    public String getResourceFileContent(String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(getResourceFile(fileName))) {
            return new String(fileInputStream.readAllBytes());
        } catch (IOException ioException) {
            return "";
        }
    }
}
