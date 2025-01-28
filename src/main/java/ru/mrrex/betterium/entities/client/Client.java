package ru.mrrex.betterium.entities.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import ru.mrrex.betterium.directories.ClientDirectory;
import ru.mrrex.betterium.directories.GameDirectory;
import ru.mrrex.betterium.directories.WorkingDirectory;
import ru.mrrex.betterium.entities.interfaces.UniqueEntity;
import ru.mrrex.betterium.runtime.JavaRuntime;

public class Client implements UniqueEntity {

    private final WorkingDirectory workingDirectory;

    private final ClientConfig clientConfig;
    private final ClientDirectory clientDirectory;

    public Client(WorkingDirectory workingDirectory, ClientConfig clientConfig, ClientDirectory clientDirectory) {
        this.workingDirectory = workingDirectory;

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

    public int run(JavaRuntime javaRuntime, ClientArguments clientArguments) throws IOException, InterruptedException {
        Path gameDirectoryPath = clientArguments.getGameDirectoryPath();

        if (Files.exists(gameDirectoryPath)) {
            throw new FileAlreadyExistsException(gameDirectoryPath.toString());
        }

        List<String> arguments = new ArrayList<>();

        Path nativesDirectoryPath = clientDirectory.getNativesDirectoryPath();
        arguments.add(String.format("-Djava.library.path=\"%s\"", nativesDirectoryPath));

        arguments.add("-cp");

        Path librariesDirectoryPath = clientDirectory.getLibrariesDirectoryPath();
        Path clientJarFilePath = clientDirectory.getClientJarFilePath();

        arguments.add(
            String.format(
                "\"%s%s*%s%s\"",
                librariesDirectoryPath,
                File.separator,
                File.pathSeparator,
                clientJarFilePath
            )
        );

        arguments.add("net.minecraft.client.Minecraft");

        String[] clientArgumentsTemplate = clientConfig.getOptions().getClientArguments();
        arguments.addAll(clientArguments.getArguments(clientArgumentsTemplate));

        GameDirectory gameDirectory = new GameDirectory(this, workingDirectory.getUserDataDirectory());
        gameDirectory.mount(gameDirectoryPath);

        int exitCode = javaRuntime.run(gameDirectoryPath, arguments);
        gameDirectory.unmount();

        return exitCode;
    }

    @Override
    public String toString() {
        return String.format(
            "Client [name=\"%s\", version=\"%s\", uid=\"%s\"]",
            clientConfig.getName(), clientConfig.getVersion(), getUniqueId()
        );
    }
}
