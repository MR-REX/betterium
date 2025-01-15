package ru.mrrex.betterium.utils;

import java.nio.file.Path;

public class Environment {
    
    private static enum OperatingSystem {
        WINDOWS, MACOS, LINUX, SOLARIS
    }

    private static OperatingSystem getOperatingSystem() {
        String operatingSystemName = System.getProperty("os.name", "").toLowerCase();

        if (operatingSystemName.contains("win"))
            return OperatingSystem.WINDOWS;

        if (operatingSystemName.contains("mac"))
            return OperatingSystem.MACOS;

        if (operatingSystemName.contains("linux") || operatingSystemName.contains("unix"))
            return OperatingSystem.LINUX;

        if (operatingSystemName.contains("solaris") || operatingSystemName.contains("sunos"))
            return OperatingSystem.SOLARIS;

        return null;
    }

    public static Path getApplicationDirectoryPath() {
        OperatingSystem operatingSystem = getOperatingSystem();
        String homeDirectory = System.getProperty("user.home", ".");

        if (operatingSystem == OperatingSystem.WINDOWS) {
            String appData = System.getenv("APPDATA");

            if (appData != null)
                return Path.of(appData);
        }

        if (operatingSystem == OperatingSystem.MACOS)
            return Path.of(homeDirectory, "Library/Application Support");

        return Path.of(homeDirectory);
    }
}
