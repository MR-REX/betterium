package ru.mrrex.betterium.entities.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import ru.mrrex.betterium.directories.ClientDirectory;
import ru.mrrex.betterium.directories.WorkingDirectory;
import ru.mrrex.betterium.entities.FileDownloadQueue;
import ru.mrrex.betterium.entities.JarFile;
import ru.mrrex.betterium.entities.MavenArtifact;
import ru.mrrex.betterium.entities.RemoteFile;
import ru.mrrex.betterium.utils.environment.Environment;
import ru.mrrex.betterium.utils.environment.OperatingSystem;
import ru.mrrex.betterium.utils.environment.ProcessorArchitecture;
import ru.mrrex.betterium.utils.environment.SystemImcompatibilityException;

public class ClientBuilder {

    private final WorkingDirectory workingDirectory;

    public ClientBuilder(WorkingDirectory workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    private void downloadRequiredFiles(ClientConfig clientConfig) throws SystemImcompatibilityException, IOException, ExecutionException {
        FileDownloadQueue downloadQueue = new FileDownloadQueue(workingDirectory);

        for (MavenArtifact mavenArtifact : clientConfig.getDependencies()) {
            downloadQueue.addDependency(mavenArtifact);
        }

        String clientUniqueID = clientConfig.getUniqueId();

        for (RemoteFile clientComponent : clientConfig.getComponents()) {
            downloadQueue.addClientComponent(clientUniqueID, clientComponent);
        }

        downloadQueue.run();
    }

    private JarFile createClientJarFile(ClientConfig clientConfig) throws FileNotFoundException, IOException {
        JarFile clientJarFile = new JarFile();
        String clientUniqueID = clientConfig.getUniqueId();

        for (RemoteFile clientComponent : clientConfig.getComponents()) {
            Path clientComponentPath = workingDirectory.getClientComponentPath(clientUniqueID, clientComponent);
            JarFile componentJarFile = JarFile.fromFile(clientComponentPath.toFile());

            clientJarFile.append(componentJarFile);
        }

        return clientJarFile;
    }

    private ClientDirectory getOrCreateClientDirectory(ClientConfig clientConfig) throws SystemImcompatibilityException, IOException, NoSuchAlgorithmException {
        OperatingSystem operatingSystem = Environment.getOperatingSystem();
        ProcessorArchitecture processorArchitecture = Environment.getProcessorArchitecture();

        String clientId = clientConfig.getUniqueId();
        ClientDirectory clientDirectory = workingDirectory.getOrCreateClientDirectory(clientId);

        for (MavenArtifact mavenArtifact : clientConfig.getDependencies()) {
            Path mavenArtifactPath = workingDirectory.getDependencyPath(mavenArtifact);
            clientDirectory.addDependency(mavenArtifactPath);

            RemoteFile[] nativeFiles = mavenArtifact.getNativeFiles(operatingSystem, processorArchitecture);

            if (nativeFiles == null && mavenArtifact.getNatives() == null) {
                throw new SystemImcompatibilityException("Unable to get native files for the current system");
            }

            for (RemoteFile nativeFile : nativeFiles) {
                Path nativeFilePath = workingDirectory.getDependencyNativeFilePath(mavenArtifact, nativeFile);
                clientDirectory.addDependencyNativeFile(nativeFilePath);
            }
        }

        if (!clientDirectory.hasValidClientJarFile()) {
            JarFile clientJarFile = createClientJarFile(clientConfig);
            clientDirectory.setClientJarFile(clientJarFile);
        }

        return clientDirectory;
    }

    public Client build(ClientConfig clientConfig) throws ClientBuildException {
        try {
            downloadRequiredFiles(clientConfig);
            ClientDirectory clientDirectory = getOrCreateClientDirectory(clientConfig);

            return new Client(clientConfig, clientDirectory);
        } catch (Exception exception) {
            throw new ClientBuildException("Failed to build client instance", exception);
        }
    }
}
