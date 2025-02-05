package ru.mrrex.betterium.runtime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ru.mrrex.betterium.utils.environment.Environment;
import ru.mrrex.betterium.utils.environment.OperatingSystem;

public class JavaRuntime {

    private final Path javaFilePath;
    private final JvmArguments jvmArguments;

    public JavaRuntime(Path javaDirectoryPath, JvmArguments jvmArguments) {
        if (javaDirectoryPath == null) {
            throw new IllegalArgumentException("JavaRuntime instance must reference the java directory");
        }

        String fileName = "java" + (Environment.getOperatingSystem() == OperatingSystem.WINDOWS ? ".exe" : "");
        Path filePath = javaDirectoryPath.resolve("bin/" + fileName);

        if (!Files.exists(filePath) && !Files.isDirectory(filePath)) {
            throw new IllegalArgumentException(String.format("The specified path does not contain \"%s\" file", fileName));
        }

        this.javaFilePath = filePath;
        this.jvmArguments = jvmArguments;
    }

    public JavaRuntime(JvmArguments jvmArguments) {
        this(
            Paths.get(System.getProperty("java.home", ".")),
            jvmArguments    
        );
    }

    public JavaRuntime() {
        this(null);
    }

    public int run(Path directoryPath, List<String> arguments) throws IOException, InterruptedException {
        if (!Files.exists(directoryPath) || ! Files.isDirectory(directoryPath)) {
            throw new FileNotFoundException(String.format("Specified working directory \"%s\" for JavaRuntime does not exist", directoryPath));
        }

        List<String> command = new ArrayList<>();

        command.add(String.format("\"%s\"", javaFilePath));

        if (jvmArguments != null) {
            command.addAll(jvmArguments.getValidArguments());
        }

        command.addAll(arguments);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(directoryPath.toFile());
        processBuilder.inheritIO();

        return processBuilder.start().waitFor();
    }

    @Override
    public String toString() {
        return String.format("JavaRuntime [path=\"%s\"]", javaFilePath);
    }
}
