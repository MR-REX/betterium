package ru.mrrex.betterium.directories;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.WeakHashMap;

import ru.mrrex.betterium.entities.MavenArtifact;
import ru.mrrex.betterium.entities.RemoteFile;
import ru.mrrex.betterium.entities.client.ClientConfig;
import ru.mrrex.betterium.utils.filesystem.DirectoryManager;

public class WorkingDirectory {

    private final Path path;

    private final Path dependenciesDirectoryPath,
                       instancesDirectoryPath;

    private final UserDataDirectory userDataDirectory;
    private final Map<String, ClientDirectory> clientDirectoriesPool;

    public WorkingDirectory(Path path) {
        this.path = DirectoryManager.defineDirectory(path);

        this.dependenciesDirectoryPath = DirectoryManager.defineDirectory(path.resolve("dependencies"));
        this.instancesDirectoryPath = DirectoryManager.defineDirectory(path.resolve("instances"));

        this.userDataDirectory = new UserDataDirectory(path.resolve("userdata"));
        this.clientDirectoriesPool = new WeakHashMap<>();
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
            clientComponent.isGlobal() ? "global" : clientUniqueId,
            clientComponent.getFileName()
        );

        return dependenciesDirectoryPath.resolve(relativePath);
    }

    public UserDataDirectory getUserDataDirectory() {
        return userDataDirectory;
    }

    public Path getClientDirectoryPath(String clientId) {
        return instancesDirectoryPath.resolve(clientId);
    }

    public ClientDirectory getClientDirectory(String clientId) {
        Path clientDirectoryPath = getClientDirectoryPath(clientId);

        if (!Files.exists(clientDirectoryPath) || !Files.isDirectory(clientDirectoryPath)) {
            return null;
        }

        if (clientDirectoriesPool.containsKey(clientId)) {
            return clientDirectoriesPool.get(clientId);
        }

        ClientDirectory clientDirectory = new ClientDirectory(clientDirectoryPath);
        clientDirectoriesPool.put(clientId, clientDirectory);

        return clientDirectory;
    }

    public ClientDirectory createClientDirectory(String clientId) throws FileAlreadyExistsException {
        Path clientDirectoryPath = getClientDirectoryPath(clientId);

        if (Files.exists(clientDirectoryPath) && Files.isDirectory(clientDirectoryPath)) {
            throw new FileAlreadyExistsException(clientDirectoryPath.toString());
        }

        ClientDirectory clientDirectory = new ClientDirectory(clientDirectoryPath);
        clientDirectoriesPool.put(clientId, clientDirectory);

        return clientDirectory;
    }

    public ClientDirectory getOrCreateClientDirectory(String clientId) {
        try {
            return createClientDirectory(clientId);
        } catch (FileAlreadyExistsException exception) {
            return getClientDirectory(clientId);
        }
    }

    @Override
    public String toString() {
        return "WorkingDirectory [path=\"%s\"]".formatted(path);
    }
}
