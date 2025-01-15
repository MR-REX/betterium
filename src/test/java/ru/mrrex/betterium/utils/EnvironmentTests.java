package ru.mrrex.betterium.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

public class EnvironmentTests {
    
    private static <T> T useSystemProperty(String key, String value, Supplier<T> supplier) {
        String defaultValue = System.getProperty(key);

        System.setProperty(key, value);
        T result = supplier.get();

        System.setProperty(key, defaultValue);

        return result;
    }

    @Test
    public void getApplicationDirectoryPathForWindows() {
        Path applicationDirectoryPath = useSystemProperty("os.name", "Windows 10", () -> Environment.getApplicationDirectoryPath());
        Path expectedDirectoryPath = Path.of(System.getenv("APPDATA"));

        assertEquals(applicationDirectoryPath, expectedDirectoryPath);
    }

    @Test
    public void getApplicationDirectoryPathForMac() {
        Path applicationDirectoryPath = useSystemProperty("os.name", "Mac OS X", () -> Environment.getApplicationDirectoryPath());

        String homeDirectory = System.getProperty("user.home", ".");
        Path expectedDirectoryPath = Path.of(homeDirectory, "Library/Application Support");

        assertEquals(applicationDirectoryPath, expectedDirectoryPath);
    }

    @Test
    public void getApplicationDirectoryPathForLinux() {
        Path applicationDirectoryPath = useSystemProperty("os.name", "Linux", () -> Environment.getApplicationDirectoryPath());
        Path expectedDirectoryPath = Path.of(System.getProperty("user.home", "."));

        assertEquals(applicationDirectoryPath, expectedDirectoryPath);
    }

    @Test
    public void getApplicationDirectoryPathForSolaris() {
        Path applicationDirectoryPath = useSystemProperty("os.name", "Solaris", () -> Environment.getApplicationDirectoryPath());
        Path expectedDirectoryPath = Path.of(System.getProperty("user.home", "."));

        assertEquals(applicationDirectoryPath, expectedDirectoryPath);
    }
}
