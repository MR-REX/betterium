package ru.mrrex.betterium.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import ru.mrrex.betterium.WorkingDirectory;
import ru.mrrex.betterium.entities.interfaces.DownloadableEntity;
import ru.mrrex.betterium.utils.environment.Environment;
import ru.mrrex.betterium.utils.environment.OperatingSystem;
import ru.mrrex.betterium.utils.environment.ProcessorArchitecture;
import ru.mrrex.betterium.utils.environment.SystemImcompatibilityException;
import ru.mrrex.betterium.utils.hash.Hash;
import ru.mrrex.betterium.utils.hash.HashAlgorithm;
import ru.mrrex.betterium.utils.network.FileDownloader;

public class FileDownloadQueue {

    private final WorkingDirectory workingDirectory;
    private final List<Callable<Void>> tasks;

    public FileDownloadQueue(WorkingDirectory workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.tasks = new ArrayList<>();
    }

    private boolean isFileAlreadyDownloaded(DownloadableEntity entity, Path filePath) {
        if (!Files.exists(filePath) && !Files.isRegularFile(filePath)) {
            return false;
        }

        try {
            return Hash.isChecksumValid(filePath, HashAlgorithm.SHA256, entity.getChecksum());
        } catch (IOException | NoSuchAlgorithmException exception) {
            return false;
        }
    }

    private void addFileDownloadTask(DownloadableEntity entity, Path filePath) throws IOException {
        if (isFileAlreadyDownloaded(entity, filePath)) {
            return;
        }

        Path parentDirectoryPath = filePath.getParent();

        if (!Files.exists(parentDirectoryPath) || !Files.isDirectory(parentDirectoryPath)) {
            Files.createDirectories(parentDirectoryPath);
        }

        Files.deleteIfExists(filePath);

        tasks.add(() -> {
            FileDownloader.downloadFileWithRetries(entity.getUrl(), filePath.toFile());
            return null;
        });
    }

    private void addDependencyNativeFiles(MavenArtifact mavenArtifact) throws SystemImcompatibilityException, IOException {
        OperatingSystem operatingSystem = Environment.getOperatingSystem();
        ProcessorArchitecture processorArchitecture = Environment.getProcessorArchitecture();

        RemoteFile[] nativeFiles = mavenArtifact.getNativeFiles(operatingSystem, processorArchitecture);

        if (nativeFiles == null) {
            throw new SystemImcompatibilityException(
                    "\"%s\" dependency requires native libraries that are not available for this system"
                            .formatted(mavenArtifact.getFullName()));
        }

        for (RemoteFile nativeFile : nativeFiles) {
            Path nativeFilePath = workingDirectory.getDependencyNativeFilePath(mavenArtifact, nativeFile);
            addFileDownloadTask(nativeFile, nativeFilePath);
        }
    }

    public void addDependency(MavenArtifact mavenArtifact) throws SystemImcompatibilityException, IOException {
        if (mavenArtifact.getNatives() != null) {
            addDependencyNativeFiles(mavenArtifact);
        }

        Path jarFilePath = workingDirectory.getDependencyPath(mavenArtifact);
        addFileDownloadTask(mavenArtifact, jarFilePath);
    }

    public void addClientComponent(String clientUniqueId, RemoteFile clientComponent) throws IOException {
        Path jarFilePath = workingDirectory.getClientComponentPath(clientUniqueId, clientComponent);
        addFileDownloadTask(clientComponent, jarFilePath);
    }

    public boolean run() throws ExecutionException {
        return new ParallelTasksExecutor().run(tasks);
    }

    public void clear() {
        tasks.clear();
    }

    @Override
    public String toString() {
        return "FileDownloadQueue [tasks=%d]".formatted(tasks.size());
    }
}
