package ru.mrrex.betterium;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import ru.mrrex.betterium.entities.MavenArtifact;
import ru.mrrex.betterium.utils.hash.Hash;
import ru.mrrex.betterium.utils.hash.HashAlgorithm;

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

    public Path getDependenciesDirectoryPath() {
        return dependenciesDirectoryPath;
    }

    public Path getMavenArtifactPath(MavenArtifact mavenArtifact) {
        Path mavenArtifactPath = dependenciesDirectoryPath.resolve(mavenArtifact.getRelativePath());

        if (!Files.exists(mavenArtifactPath) || !Files.isRegularFile(mavenArtifactPath)) {
            return null;
        }

        return mavenArtifactPath;
    }

    public boolean hasMavenArtifact(MavenArtifact mavenArtifact) {
        Path mavenArtifactPath = getMavenArtifactPath(mavenArtifact);

        if (mavenArtifactPath == null) {
            return false;
        }

        try {
            return Hash.isChecksumValid(mavenArtifactPath, HashAlgorithm.SHA256, mavenArtifact.getChecksum());
        } catch (IOException | NoSuchAlgorithmException exception) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "WorkingDirectory [path=\"%s\"]".formatted(path);
    }
}
