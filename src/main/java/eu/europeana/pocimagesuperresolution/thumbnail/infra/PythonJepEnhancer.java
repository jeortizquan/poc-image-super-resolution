package eu.europeana.pocimagesuperresolution.thumbnail.infra;

import eu.europeana.pocimagesuperresolution.thumbnail.common.ResourceHandler;
import eu.europeana.pocimagesuperresolution.thumbnail.domain.model.ThumbnailEnhancer;
import jep.Interpreter;
import jep.SharedInterpreter;

/**
 * The type Python jep enhancer.
 */
public class PythonJepEnhancer implements ThumbnailEnhancer {
    private final String model;
    private final ResourceHandler resourceHandler;

    /**
     * Instantiates a new Python jep enhancer.
     *
     * @param resourceHandler the resource handler
     * @param model           the model
     * This needs the following commands:
     * * add to metis project maven jep database
     *   <!-- https://mvnrepository.com/artifact/black.ninia/jep -->
     *   <dependency>
     *     <groupId>black.ninia</groupId>
     *     <artifactId>jep</artifactId>
     *     <version>4.1.1</version>
     *   </dependency>
     * * in the 'production' server must exist:
     *   * (python interpreter)              => python 3.10
     *   * (python package manager)          => pip
     *   * (img super resolution dependency) => pip ISR install or python3 ../image-super-resolution/setup.py install
     *   * (environment variable)            => export JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64"
     *   * (java python interpreter)         => pip install jep
     */
    public PythonJepEnhancer(ResourceHandler resourceHandler, String model) {
        this.resourceHandler = resourceHandler;
        this.model = model;
    }

    @Override
    public String enhance(String inputFile) {
        final String outputFile = resourceHandler.generateOutputFileName(inputFile);
        final String pythonScript = resourceHandler.getResourceFileContent("scripts/isr_lib.py")
                .replace("{:0}", inputFile)
                .replace("{:1}", outputFile);
        try (Interpreter interpreter = new SharedInterpreter()) {
            interpreter.exec(pythonScript.replace("{:2}", model));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return outputFile;
    }
}
