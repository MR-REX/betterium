package ru.mrrex.betterium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ru.mrrex.betterium.entities.MavenArtifact;
import ru.mrrex.betterium.entities.RemoteFile;
import ru.mrrex.betterium.entities.client.Client;
import ru.mrrex.betterium.entities.client.ClientBuildException;
import ru.mrrex.betterium.entities.client.ClientConfig;
import ru.mrrex.betterium.entities.client.ClientOptions;
import ru.mrrex.betterium.utils.environment.Environment;

public class BetteriumTests {

    @Test
    public void createBetteriumInstanceWithDefaultWorkingDirectory() {
        Path defaultWorkingDirectoryPath = Environment.getApplicationDirectoryPath().resolve(".betterium");
        Betterium betterium = new Betterium();

        assertEquals(betterium.getWorkingDirectoryPath(), defaultWorkingDirectoryPath);
    }

    @Test
    public void createBetteriumInstanceWithCustomWorkingDirectory(@TempDir Path temporaryDirectoryPath) {
        Betterium betterium = new Betterium(temporaryDirectoryPath);
        assertEquals(betterium.getWorkingDirectoryPath(), temporaryDirectoryPath);
    }

    @Test
    public void loadClientConfig(@TempDir Path temporaryDirectoryPath) throws StreamReadException, DatabindException, IOException {
        ClientConfig emptyClientConfig = new ClientConfig();
        Path tempClientConfigPath = temporaryDirectoryPath.resolve("client-config.json");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());

        objectWriter.writeValue(tempClientConfigPath.toFile(), emptyClientConfig);

        Betterium betterium = new Betterium(temporaryDirectoryPath);
        
        ClientConfig loadedClientConfig = betterium.loadClientConfig(tempClientConfigPath);
        assertEquals(emptyClientConfig.getUniqueId(), loadedClientConfig.getUniqueId());
    }

    @Test
    public void createClientFromClientConfig(@TempDir Path temporaryDirectoryPath) throws ClientBuildException {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setName("Test Client Name");
        clientConfig.setComponents(new RemoteFile[0]);
        clientConfig.setDependencies(new MavenArtifact[0]);

        ClientOptions clientOptions = new ClientOptions();
        clientOptions.setClientArguments(new String[0]);
        clientOptions.setJvmArguments(new String[0]);
        clientConfig.setOptions(clientOptions);
        
        Betterium betterium = new Betterium(temporaryDirectoryPath);
        Client client = betterium.createClient(clientConfig);

        assertNotNull(client);
        assertEquals(client.getUniqueId(), clientConfig.getUniqueId());
    }
}
