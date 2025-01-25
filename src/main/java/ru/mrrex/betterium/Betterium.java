package ru.mrrex.betterium;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.mrrex.betterium.directories.WorkingDirectory;
import ru.mrrex.betterium.entities.client.Client;
import ru.mrrex.betterium.entities.client.ClientBuildException;
import ru.mrrex.betterium.entities.client.ClientBuilder;
import ru.mrrex.betterium.entities.client.ClientConfig;
import ru.mrrex.betterium.utils.environment.Environment;

public class Betterium {

    public static final String DEFAULT_WORKING_DIRECTORY_NAME = ".betterium";

    private final WorkingDirectory workingDirectory;
    private final ObjectMapper objectMapper;
    private final ClientBuilder clientBuilder;

    public Betterium(Path workingDirectoryPath) {
        workingDirectory = new WorkingDirectory(workingDirectoryPath);
        objectMapper = new ObjectMapper();
        clientBuilder = new ClientBuilder(workingDirectory);
    }

    public Betterium() {
        this(Environment.getApplicationDirectoryPath().resolve(DEFAULT_WORKING_DIRECTORY_NAME));
    }

    public Path getWorkingDirectoryPath() {
        return workingDirectory.getPath();
    }

    public ClientConfig loadClientConfig(Path clientConfigPath) throws StreamReadException, DatabindException, IOException {
        try (InputStream inputStream = Files.newInputStream(clientConfigPath)) {
            return objectMapper.readValue(inputStream, ClientConfig.class);
        }
    }

    public Client createClient(ClientConfig clientConfig) throws ClientBuildException {
        return clientBuilder.build(clientConfig);
    }

    @Override
    public String toString() {
        return "Betterium [path=\"%s\"]".formatted(workingDirectory.getPath());
    }
}
