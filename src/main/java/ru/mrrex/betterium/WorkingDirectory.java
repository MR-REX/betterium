package ru.mrrex.betterium;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkingDirectory {

    private final Path path;

    private final Path dependenciesDirectoryPath,
                       instancesDirectoryPath,
                       screenshotsDirectoryPath;

    public WorkingDirectory(Path path) {
        this.path = path;

        this.dependenciesDirectoryPath = path.resolve("dependencies");
        this.instancesDirectoryPath = path.resolve("instances");
        this.screenshotsDirectoryPath = path.resolve("screenshots");

        try {
            createDirectoriesIfNoExists();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to initialize working directory", exception);
        }
    }

    private Path[] getDirectoryPaths() {
        return new Path[] {
            path,
            dependenciesDirectoryPath,
            instancesDirectoryPath,
            screenshotsDirectoryPath
        };
    }

    private void createDirectoriesIfNoExists() throws IOException {
        for (Path directoryPath : getDirectoryPaths()) {
            if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
                continue;
            }

            Files.createDirectory(directoryPath);
        }
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "WorkingDirectory [path=\"%s\"]".formatted(path);
    }
}
