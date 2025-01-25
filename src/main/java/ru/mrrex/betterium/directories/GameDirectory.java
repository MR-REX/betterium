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

    private class RestoreFile {
        private static final String FILENAME = ".betterium";

        private String getFileContent() {
            return client.getUniqueId();
        }

        private void write(Path filePath) throws IOException {
            Files.writeString(filePath, getFileContent(), StandardCharsets.UTF_8);
        }

        private static String read(Path filePath) throws IOException {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        }

        private boolean equals(Path filePath) throws IOException {
            return read(filePath).equals(getFileContent());
        }

        @Override
        public String toString() {
            return "RestoreFile [client_id=\"%s\"]".formatted(client.getUniqueId());
        }
    }

    private final Client client;
    private final UserDataDirectory userDataDirectory;
    private final RestoreFile restoreFile;

    private Path path;

    public GameDirectory(Client client, UserDataDirectory userDataDirectory, Path directoryPath) {
        this.client = client;
        this.userDataDirectory = userDataDirectory;
        this.restoreFile = new RestoreFile();

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
        DirectoryManager.copyDirectory(userDataDirectory.getScreenshotsDirectoryPath(), path.resolve("screenshots"));
        DirectoryManager.copyDirectory(userDataDirectory.getTexturepacksDirectoryPath(), path.resolve("texturepacks"));

        Path restoreFilePath = path.resolve(RestoreFile.FILENAME);
        restoreFile.write(restoreFilePath);

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

        if (!restoreFile.equals(restoreFilePath)) {
            throw new IOException("Recovery file content do not match the current configuration");
        }

        Files.deleteIfExists(restoreFilePath);

        DirectoryManager.moveDirectory(path.resolve("screenshots"), userDataDirectory.getScreenshotsDirectoryPath());
        DirectoryManager.moveDirectory(path.resolve("texturepacks"), userDataDirectory.getTexturepacksDirectoryPath());
        DirectoryManager.moveDirectory(path, client.getDataDirectoryPath());

        if (Files.exists(path) && Files.isDirectory(path)) {
            DirectoryManager.deleteDirectory(path);
        }

        path = null;
    }

    @Override
    public String toString() {
        return "GameDirectory [client_id=\"%s\", mounted=%s]".formatted(client.getUniqueId(), isMounted());
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
