package ru.mrrex.betterium.runtime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import ru.mrrex.betterium.entities.client.Client;
import ru.mrrex.betterium.utils.environment.Environment;
import ru.mrrex.betterium.utils.environment.OperatingSystem;

public class JavaRuntime {

    private final Path javaFilePath;

    public JavaRuntime(Path javaDirectoryPath) {
        if (javaDirectoryPath == null) {
            throw new IllegalArgumentException("JavaRuntime instance must reference the java directory");
        }

        String fileName = "java" + (Environment.getOperatingSystem() == OperatingSystem.WINDOWS ? ".exe" : "");
        Path filePath = javaDirectoryPath.resolve("bin/" + fileName);

        if (!Files.exists(filePath) && !Files.isDirectory(filePath)) {
            throw new IllegalArgumentException("The specified path does not contain \"%s\" file".formatted(fileName));
        }

        this.javaFilePath = filePath;
    }

    public JavaRuntime() {
        this(Path.of(System.getProperty("java.home", ".")));
    }

    private int run(List<String> command) throws IOException, InterruptedException {
        command.add(0, javaFilePath.toString());

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO();

        return processBuilder.start().waitFor();
    }

    public int run(Client client) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();

        Path nativesDirectoryPath = client.getNativesDirectoryPath();
        command.add("-Djava.library.path=\"%s\"".formatted(nativesDirectoryPath));

        command.add("-cp");

        Path librariesDirectoryPath = client.getLibrariesDirectoryPath();
        Path clientJarFilePath = client.getClientJarFilePath();
        command.add("\"%s%s*%s%s\"".formatted(librariesDirectoryPath, File.pathSeparator, File.pathSeparatorChar, clientJarFilePath));

        command.add("net.minecraft.client.Minecraft");
        command.add("Meow");

        return run(command);
    }

    @Override
    public String toString() {
        return "JavaRuntime [path=\"%s\"]".formatted(javaFilePath);
    }
}
