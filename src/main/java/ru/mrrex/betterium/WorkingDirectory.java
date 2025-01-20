package ru.mrrex.betterium;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import ru.mrrex.betterium.entities.MavenArtifact;
import ru.mrrex.betterium.entities.RemoteFile;
import ru.mrrex.betterium.entities.client.ClientConfig;
import ru.mrrex.betterium.entities.interfaces.DownloadableEntity;
import ru.mrrex.betterium.utils.environment.Environment;
import ru.mrrex.betterium.utils.environment.OperatingSystem;
import ru.mrrex.betterium.utils.environment.ProcessorArchitecture;
import ru.mrrex.betterium.utils.environment.SystemImcompatibilityException;
import ru.mrrex.betterium.utils.hash.Hash;
import ru.mrrex.betterium.utils.hash.HashAlgorithm;
import ru.mrrex.betterium.utils.network.FileDownloader;
import ru.mrrex.betterium.utils.network.RetryLimitExceededException;

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

    public Path getDependencyPath(MavenArtifact mavenArtifact) {
        String relativePath = "libraries/%s/%s/%s/%s".formatted(
            mavenArtifact.getGroupId(),
            mavenArtifact.getArtifactId(),
            mavenArtifact.getVersion(),
            mavenArtifact.getFileName()
        );

        return dependenciesDirectoryPath.resolve(relativePath);
    }

    public Path getDependencyNativeFilePath(MavenArtifact mavenArtifact, RemoteFile nativeFile) {
        String relativePath = "natives/%s/%s/%s/%s".formatted(
            mavenArtifact.getGroupId(),
            mavenArtifact.getArtifactId(),
            mavenArtifact.getVersion(),
            nativeFile.getFileName()
        );

        return dependenciesDirectoryPath.resolve(relativePath);
    }

    public Path getClientComponentPath(String clientUniqueId, RemoteFile clientComponent) {
        String relativePath = "components/%s/%s".formatted(
            clientUniqueId,
            clientComponent.getFileName()
        );

        return dependenciesDirectoryPath.resolve(relativePath);
    }

    @Override
    public String toString() {
        return "WorkingDirectory [path=\"%s\"]".formatted(path);
    }
}
