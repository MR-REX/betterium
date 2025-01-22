package ru.mrrex.betterium.entities.client;

import java.nio.file.Path;

import ru.mrrex.betterium.directories.ClientDirectory;
import ru.mrrex.betterium.entities.interfaces.UniqueEntity;

public class Client implements UniqueEntity {

    private final ClientConfig clientConfig;
    private final ClientDirectory clientDirectory;

    public Client(ClientConfig clientConfig, ClientDirectory clientDirectory) {
        this.clientConfig = clientConfig;
        this.clientDirectory = clientDirectory;
    }

    public String getUniqueId() {
        return clientConfig.getUniqueId();
    }

    public Path getLibrariesDirectoryPath() {
        return clientDirectory.getLibrariesDirectoryPath();
    }

    public Path getNativesDirectoryPath() {
        return clientDirectory.getNativesDirectoryPath();
    }

    public Path getDataDirectoryPath() {
        return clientDirectory.getDataDirectoryPath();
    }

    public Path getClientJarFilePath() {
        return clientDirectory.getClientJarFilePath();
    }

    @Override
    public String toString() {
        return "Client [name=\"%s\", version=\"%s\", uid=\"%s\"]".formatted(
            clientConfig.getName(), clientConfig.getVersion(), getUniqueId()
        );
    }
}
