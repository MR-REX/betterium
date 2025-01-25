package ru.mrrex.betterium.directories;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import ru.mrrex.betterium.entities.JarFile;
import ru.mrrex.betterium.utils.filesystem.DirectoryManager;
import ru.mrrex.betterium.utils.hash.Hash;
import ru.mrrex.betterium.utils.hash.HashAlgorithm;

public class ClientDirectory {

    private final Path path;

    private final Path librariesDirectoryPath,
                       nativesDirectoryPath,
                       dataDirectoryPath;

    public ClientDirectory(Path path) {
        this.path = DirectoryManager.defineDirectory(path);

        this.librariesDirectoryPath = DirectoryManager.defineEmptyDirectory(path.resolve("libraries"));
        this.nativesDirectoryPath = DirectoryManager.defineEmptyDirectory(path.resolve("natives"));
        this.dataDirectoryPath = DirectoryManager.defineDirectory(path.resolve("data"));
    }

    public Path getPath() {
        return path;
    }

    private void createHardLink(Path directoryPath, Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString();
        Path hardLinkPath = directoryPath.resolve(fileName);

        if (Files.exists(hardLinkPath)) {
            return;
        }

        Files.createLink(hardLinkPath, filePath);
    }

    public void addDependency(Path mavenArtifactPath) throws IOException {
        createHardLink(librariesDirectoryPath, mavenArtifactPath);
    }

    public void addDependencyNativeFile(Path nativeFilePath) throws IOException {
        createHardLink(nativesDirectoryPath, nativeFilePath);
    }

    public Path getClientJarFilePath() {
        return path.resolve("client.jar");
    }

    public Path getClientChecksumFilePath() {
        return path.resolve("checksum.bin");
    }

    public void setClientJarFile(JarFile clientJarFile) throws IOException, NoSuchAlgorithmException {
        Path clientJarFilePath = getClientJarFilePath();

        Files.deleteIfExists(clientJarFilePath);
        clientJarFile.write(clientJarFilePath.toFile());

        byte[] checksum = Hash.getHash(clientJarFilePath, HashAlgorithm.SHA256);
        Path checksumFilePath = getClientChecksumFilePath();

        Files.write(checksumFilePath, checksum);
    }

    public boolean hasValidClientJarFile() {
        Path clientJarFilePath = getClientJarFilePath();

        if (!Files.exists(clientJarFilePath) || !Files.isRegularFile(clientJarFilePath)) {
            return false;
        }

        Path checksumFilePath = getClientChecksumFilePath();

        if (!Files.exists(checksumFilePath) || !Files.isRegularFile(checksumFilePath)) {
            return false;
        }

        try {
            String expectedChecksum = Hash.toHexString(Files.readAllBytes(checksumFilePath));
            return Hash.isChecksumValid(clientJarFilePath, HashAlgorithm.SHA256, expectedChecksum);
        } catch (IOException | NoSuchAlgorithmException exception) {
            return false;
        }
    }

    public Path getLibrariesDirectoryPath() {
        return librariesDirectoryPath;
    }

    public Path getNativesDirectoryPath() {
        return nativesDirectoryPath;
    }

    public Path getDataDirectoryPath() {
        return dataDirectoryPath;
    }

    @Override
    public String toString() {
        return "ClientDirectory [path=\"%s\"]".formatted(path);
    }
}
