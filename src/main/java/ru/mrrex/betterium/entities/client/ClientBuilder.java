package ru.mrrex.betterium.entities.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ru.mrrex.betterium.Betterium;
import ru.mrrex.betterium.entities.FileDownloadQueue;
import ru.mrrex.betterium.entities.MavenArtifact;
import ru.mrrex.betterium.entities.RemoteFile;
import ru.mrrex.betterium.utils.environment.SystemImcompatibilityException;

public class ClientBuilder {

    private final Betterium betterium;

    public ClientBuilder(Betterium betterium) {
        this.betterium = betterium;
    }

    private void prepareRequiredFiles(ClientConfig clientConfig) throws SystemImcompatibilityException, IOException, ExecutionException {
        FileDownloadQueue downloadQueue = betterium.createFileDownloadQueue();

        for (MavenArtifact mavenArtifact : clientConfig.getDependencies()) {
            downloadQueue.addDependency(mavenArtifact);
        }

        String clientUniqueID = clientConfig.getUniqueId();

        for (RemoteFile clientComponent : clientConfig.getComponents()) {
            downloadQueue.addClientComponent(clientUniqueID, clientComponent);
        }

        downloadQueue.run();
    }

    public Client build(ClientConfig clientConfig) throws ClientBuildException {
        try {
            prepareRequiredFiles(clientConfig);
        } catch (Exception exception) {
            throw new ClientBuildException("Failed to prepare required client files", exception);
        }



        return null;
    }
}
