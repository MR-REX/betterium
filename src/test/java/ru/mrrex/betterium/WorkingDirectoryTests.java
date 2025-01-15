package ru.mrrex.betterium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class WorkingDirectoryTests {

    @Test
    public void validateWorkingDirectoryPath(@TempDir Path temporaryDirectoryPath) {
        assertEquals(new WorkingDirectory(temporaryDirectoryPath).getPath(), temporaryDirectoryPath);
    }

    @Test
    public void validateDirectoriesInitialization(@TempDir Path temporaryDirectoryPath) {
        new WorkingDirectory(temporaryDirectoryPath);

        Path[] internalDirectories = {
            temporaryDirectoryPath.resolve("dependencies"),
            temporaryDirectoryPath.resolve("instances"),
            temporaryDirectoryPath.resolve("screenshots")
        };

        boolean allDirectoriesExists = true;

        for (Path internalDirectoryPath : internalDirectories) {
            if (!Files.exists(internalDirectoryPath) || !Files.isDirectory(internalDirectoryPath)) {
                allDirectoriesExists = false;
                break;
            }
        }

        assertTrue(allDirectoriesExists);
    }
}
