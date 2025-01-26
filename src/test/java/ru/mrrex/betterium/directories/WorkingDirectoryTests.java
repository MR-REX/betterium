package ru.mrrex.betterium.directories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ru.mrrex.betterium.entities.MavenArtifact;
import ru.mrrex.betterium.entities.RemoteFile;

public class WorkingDirectoryTests {

    @Test
    public void validateWorkingDirectoryPath(@TempDir Path temporaryDirectoryPath) {
        assertEquals(new WorkingDirectory(temporaryDirectoryPath).getPath(), temporaryDirectoryPath);
    }

    @Test
    public void validateInternalDirectoriesInitialization(@TempDir Path temporaryDirectoryPath) {
        WorkingDirectory workingDirectory = new WorkingDirectory(temporaryDirectoryPath);
        Path workingDirectoryPath = workingDirectory.getPath();

        Path[] internalDirectories = {
            workingDirectoryPath.resolve("dependencies"),
            workingDirectoryPath.resolve("instances"),
            workingDirectoryPath.resolve("userdata")
        };

        assertTrue(Arrays.stream(internalDirectories).allMatch(path -> Files.exists(path) && Files.isDirectory(path)));
    }

    @Test
    public void validateDependencyPaths(@TempDir Path temporaryDirectoryPath) throws MalformedURLException {
        MavenArtifact mavenArtifact = new MavenArtifact();
        mavenArtifact.setArtifactId("artifact.id");
        mavenArtifact.setGroupId("group.id");
        mavenArtifact.setVersion("1.0.0");
        mavenArtifact.setChecksum("N/A");
        mavenArtifact.setNatives(new HashMap<>());

        Path expectedDependencyDirectoryPath = Path.of(
            temporaryDirectoryPath.toString(),
            "dependencies/libraries",
            mavenArtifact.getGroupId(),
            mavenArtifact.getArtifactId(),
            mavenArtifact.getVersion(),
            mavenArtifact.getFileName()
        );

        RemoteFile nativeFile = new RemoteFile();
        nativeFile.setUrl(new URL("http://localhost/native.dll"));

        Path expectedNativesDirectoryPath = Path.of(
            temporaryDirectoryPath.toString(),
            "dependencies/natives",
            mavenArtifact.getGroupId(),
            mavenArtifact.getArtifactId(),
            mavenArtifact.getVersion(),
            nativeFile.getFileName()
        );

        WorkingDirectory workingDirectory = new WorkingDirectory(temporaryDirectoryPath);

        assertEquals(workingDirectory.getDependencyPath(mavenArtifact), expectedDependencyDirectoryPath);
        assertEquals(workingDirectory.getDependencyNativeFilePath(mavenArtifact, nativeFile), expectedNativesDirectoryPath);
    }

    @Test
    public void validateUserDataDirectoryPath(@TempDir Path temporaryDirectoryPath) {
        WorkingDirectory workingDirectory = new WorkingDirectory(temporaryDirectoryPath);
        Path userDataDirectoryPath = temporaryDirectoryPath.resolve("userdata");

        assertEquals(workingDirectory.getUserDataDirectory().getPath(), userDataDirectoryPath);
    }

    @Test
    public void validateClientDirectoryPath(@TempDir Path temporaryDirectoryPath) {
        WorkingDirectory workingDirectory = new WorkingDirectory(temporaryDirectoryPath);
        
        String clientId = UUID.randomUUID().toString().replace("-", "");
        Path clientDirectoryPath = temporaryDirectoryPath.resolve("instances/" + clientId);

        assertEquals(workingDirectory.getClientDirectoryPath(clientId), clientDirectoryPath);
    }
}
