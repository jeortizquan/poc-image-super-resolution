package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

/**
 * The type Python graal vm enhancer.
 */
public class PythonGraalVMEnhancer implements ThumbnailEnhancer {
    private static String PYTHON = "python";
    private static String VENV_EXECUTABLE = PythonGraalVMEnhancer.class.getClassLoader().getResource(Paths.get("venv", "bin", "graalpy").toString()).getPath();
    private static String SOURCE_FILE_NAME = "isr_lib.py";
    private final ResourceHandler resourceHandler;
    private final String model;


    /**
     * Instantiates a new Python graal vm enhancer.
     *
     * @param resourceHandler the resource handler
     * @param model           the model
     */
    public PythonGraalVMEnhancer(ResourceHandler resourceHandler, String model) {
        this.resourceHandler = resourceHandler;
        this.model = model;
    }

    /**
     * Create python enhancer string.
     *
     * @param model     the model
     * @param inputFile the input file
     * @return the string
     */
    String createPythonEnhancer(String model, String inputFile) {
        Context context = Context.newBuilder(PYTHON).
                // It is a good idea to start with allowAllAccess(true) and only when everything is
                // working to start trying to reduce it. See the GraalVM docs for fine-grained
                // permissions.
                        allowAllAccess(true).
                // Python virtualenvs work by setting up their initial package paths based on the
                // runtime path of the python executable. Since we are not executing from the python
                // executable, we need to set this option to what it would be
                        option("python.Executable", VENV_EXECUTABLE).
                // The actual package setup only happens inside Python's "site" module. This module is
                // automatically imported when starting the Python executable, but there is an option
                // to turn this off even for the executable. To avoid accidental file system access, we
                // do not import this module by default. Setting this option to true after setting the
                // python.Executable option ensures we import the site module at startup, but only
                // within the virtualenv.
                        option("python.ForceImportSite", "true").
                build();
        final String outputFile = resourceHandler.generateOutputFileName(inputFile);
        final String pythonScript = resourceHandler.getResourceFileContent("scripts/isr_lib.py")
                .replace("{:0}", inputFile)
                .replace("{:1}", outputFile);
        File tempFile = resourceHandler.createPythonTempFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {

            fileOutputStream.write(pythonScript.replace("{:2}", model).getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader code = null;
        try {
            code = new InputStreamReader(new FileInputStream(tempFile.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Source source;
        try {
            source = Source.newBuilder(PYTHON, code, SOURCE_FILE_NAME).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Value sysCode = context.eval("python", pythonScript.replace("{:2}", model));
        context.close();
        return outputFile;
    }

    @Override
    public String enhance(String input) {
        return createPythonEnhancer(model, input);
    }

    @Override
    public List<String> enhance(List<String> inputFile) {
        return null;
    }
}
