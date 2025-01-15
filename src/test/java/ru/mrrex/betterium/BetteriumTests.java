package ru.mrrex.betterium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ru.mrrex.betterium.utils.Environment;

public class BetteriumTests {

    @Test
    public void createBetteriumInstanceWithDefaultWorkingDirectory() {
        Path defaultWorkingDirectoryPath = Environment.getApplicationDirectoryPath().resolve(".betterium");
        String expectedStringRepresentation = "Betterium [path=\"%s\"]".formatted(defaultWorkingDirectoryPath);

        Betterium betterium = new Betterium();
        assertEquals(betterium.toString(), expectedStringRepresentation);
    }

    @Test
    public void createBetteriumInstanceWithCustomWorkingDirectory(@TempDir Path temporaryDirectoryPath) {
        String expectedStringRepresentation = "Betterium [path=\"%s\"]".formatted(temporaryDirectoryPath);

        Betterium betterium = new Betterium(temporaryDirectoryPath);
        assertEquals(betterium.toString(), expectedStringRepresentation);
    }
}
