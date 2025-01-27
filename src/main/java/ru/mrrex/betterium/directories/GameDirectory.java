package ru.mrrex.betterium.directories;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import ru.mrrex.betterium.entities.client.Client;
import ru.mrrex.betterium.entities.client.ClientConfig;
import ru.mrrex.betterium.utils.filesystem.DirectoryManager;

public class GameDirectory {

    private static class RestoreFile {
        private static final String FILENAME = ".betterium";

        private static String getFileContent(GameDirectory gameDirectory) {
            return gameDirectory.client.getUniqueId();
        }

        private static void write(GameDirectory gameDirectory, Path filePath) throws IOException {
            byte[] bytes = getFileContent(gameDirectory).getBytes(StandardCharsets.UTF_8);
            Files.write(filePath, bytes);
        }

        private static String read(Path filePath) throws IOException {
            byte[] bytes = Files.readAllBytes(filePath);
            return new String(bytes, StandardCharsets.UTF_8);
        }

        private static boolean equals(GameDirectory gameDirectory, Path filePath) throws IOException {
            return read(filePath).equals(getFileContent(gameDirectory));
        }
    }

    private final Client client;
    private final UserDataDirectory userDataDirectory;

    private Path path;

    public GameDirectory(Client client, UserDataDirectory userDataDirectory, Path directoryPath) {
        this.client = client;
        this.userDataDirectory = userDataDirectory;

        this.path = directoryPath;
    }

    public GameDirectory(Client client, UserDataDirectory userDataDirectory) {
        this(client, userDataDirectory, null);
    }

    public boolean isMounted() {
        return path != null;
    }

    public Path getPath() {
        return path;
    }

    public void mount(Path path) throws IOException {
        if (this.path != null) {
            throw new IOException("Attempt to remount a mounted game directory");
        }

        if (Files.exists(path)) {
            throw new FileAlreadyExistsException(path.toString());
        }

        DirectoryManager.copyDirectory(client.getDataDirectoryPath(), path);

        for (Path internalDirectoryPath : userDataDirectory.getInternalDirectoriesPaths()) {
            DirectoryManager.copyDirectory(internalDirectoryPath, path.resolve(internalDirectoryPath.getFileName()));
        }

        Path restoreFilePath = path.resolve(RestoreFile.FILENAME);
        RestoreFile.write(this, restoreFilePath);

        this.path = path;
    }

    public void unmount() throws IOException {
        if (path == null) {
            throw new IOException("Game directory was not mounted");
        }

        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new FileNotFoundException("Couldn't find the mounted game directory");
        }

        Path restoreFilePath = path.resolve(RestoreFile.FILENAME);

        if (!Files.exists(restoreFilePath) || !Files.isRegularFile(restoreFilePath)) {
            throw new FileNotFoundException("Couldn't find the restore file");
        }

        if (!RestoreFile.equals(this, restoreFilePath)) {
            throw new IOException("Recovery file content do not match the current configuration");
        }

        Files.deleteIfExists(restoreFilePath);

        for (Path internalDirectoryPath : userDataDirectory.getInternalDirectoriesPaths()) {
            DirectoryManager.moveDirectory(path.resolve(internalDirectoryPath.getFileName()), internalDirectoryPath);
        }

        DirectoryManager.moveDirectory(path, client.getDataDirectoryPath());

        if (Files.exists(path) && Files.isDirectory(path)) {
            DirectoryManager.deleteDirectory(path);
        }

        path = null;
    }

    @Override
    public String toString() {
        return String.format("GameDirectory [client_id=\"%s\", mounted=%s]", client.getUniqueId(), isMounted());
    }

    public static GameDirectory restoreFromDirectory(WorkingDirectory workingDirectory, Path directoryPath) throws IOException {
        if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
            throw new FileNotFoundException("Directory on the specified path does not exist");
        }

        Path restoreFilePath = directoryPath.resolve(RestoreFile.FILENAME);

        if (!Files.exists(restoreFilePath) || !Files.isRegularFile(restoreFilePath)) {
            throw new FileNotFoundException("Couldn't find the restore file");
        }

        String clientId = RestoreFile.read(restoreFilePath);

        if (!workingDirectory.hasClientDirectory(clientId)) {
            throw new ClientInstanceNotFound(clientId);
        }

        ClientDirectory clientDirectory = workingDirectory.getClientDirectory(clientId);
        UserDataDirectory userDataDirectory = workingDirectory.getUserDataDirectory();

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setName("N/A");
        clientConfig.setVersion("N/A");
        clientConfig.setUniqueId(clientId);

        Client client = new Client(workingDirectory, clientConfig, clientDirectory);

        return new GameDirectory(client, userDataDirectory, directoryPath);
    }
}
