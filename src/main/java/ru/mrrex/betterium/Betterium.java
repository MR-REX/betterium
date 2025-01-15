package ru.mrrex.betterium;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.mrrex.betterium.entities.ClientInstance;
import ru.mrrex.betterium.utils.Environment;

public class Betterium {

    public static final String DEFAULT_WORKING_DIRECTORY_NAME = ".betterium";

    private final WorkingDirectory workingDirectory;
    private final ObjectMapper objectMapper;

    public Betterium(Path workingDirectoryPath) {
        workingDirectory = new WorkingDirectory(workingDirectoryPath);
        objectMapper = new ObjectMapper();
    }

    public Betterium() {
        this(Environment.getApplicationDirectoryPath().resolve(DEFAULT_WORKING_DIRECTORY_NAME));
    }

    public ClientInstance createClientInstance(Path clientInstanceConfigPath) throws StreamReadException, DatabindException, IOException {
        try (InputStream inputStream = Files.newInputStream(clientInstanceConfigPath)) {
            return objectMapper.readValue(inputStream, ClientInstance.class);
        }
    }

    @Override
    public String toString() {
        return "Betterium [path=\"%s\"]".formatted(workingDirectory.getPath());
    }
}
