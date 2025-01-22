package ru.mrrex.betterium.utils.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.management.RuntimeErrorException;

import ru.mrrex.betterium.directories.ClientDirectory;
import ru.mrrex.betterium.directories.UserDataDirectory;
import ru.mrrex.betterium.directories.WorkingDirectory;
import ru.mrrex.betterium.entities.client.Client;
import ru.mrrex.betterium.utils.environment.Environment;

public class ProxyGameDirectory {

    private static Path getGameDirectoryPath() {
        return Environment.getApplicationDirectoryPath().resolve(".minecraft-bta");
    }

    private static Path getClientIdFilePath() {
        return getGameDirectoryPath().resolve("betterium.bin");
    }

    public static boolean isMounted() {
        Path gameDirectoryPath = getGameDirectoryPath();

        if (!Files.exists(gameDirectoryPath) || !Files.isDirectory(gameDirectoryPath)) {
            return false;
        }

        Path clientIdFilePath = getClientIdFilePath();

        if (!Files.exists(clientIdFilePath) || !Files.isRegularFile(clientIdFilePath)) {
            return false;
        }

        return true;
    }

    public static void mount(Client client, UserDataDirectory userDataDirectory) throws IOException {
        Path gameDirectoryPath = getGameDirectoryPath();

        if (Files.exists(gameDirectoryPath) && Files.isDirectory(gameDirectoryPath)) {
            throw new FileAlreadyExistsException(gameDirectoryPath.toString());
        }

        DirectoryManager.copyDirectory(client.getDataDirectoryPath(), gameDirectoryPath);

        DirectoryManager.copyDirectory(userDataDirectory.getScreenshotsDirectoryPath(), gameDirectoryPath.resolve("screenshots"));
        DirectoryManager.copyDirectory(userDataDirectory.getTexturepacksDirectoryPath(), gameDirectoryPath.resolve("texturepacks"));

        Path clientIdFilePath = getClientIdFilePath();

        byte[] clientIdToBytes = client.getUniqueId().getBytes(StandardCharsets.UTF_8);
        Files.write(clientIdFilePath, clientIdToBytes);
    }

    public static void unmount(WorkingDirectory workingDirectory) throws IOException {
        if (!isMounted()) {
            throw new FileNotFoundException("An attempt to unmount a non-existent proxy game directory");
        }

        Path gameDirectoryPath = getGameDirectoryPath();

        Path clientIdFilePath = getClientIdFilePath();
        String clientId = new String(Files.readAllBytes(clientIdFilePath), StandardCharsets.UTF_8);

        Path clientDirectoryPath = workingDirectory.getClientDirectoryPath(clientId);

        if (!Files.exists(clientDirectoryPath) || !Files.isDirectory(clientDirectoryPath)) {
            throw new FileNotFoundException("Mounted proxy game directory refers to a non-existent version of the client");
        }

        
    }
}
