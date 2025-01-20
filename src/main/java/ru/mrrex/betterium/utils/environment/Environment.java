package ru.mrrex.betterium.utils.environment;

import java.nio.file.Path;

public class Environment {

    public static OperatingSystem getOperatingSystem() {
        String operatingSystemName = System.getProperty("os.name", "").toLowerCase();

        if (operatingSystemName.contains("win"))
            return OperatingSystem.WINDOWS;

        if (operatingSystemName.contains("mac"))
            return OperatingSystem.MACOSX;

        if (operatingSystemName.contains("linux") || operatingSystemName.contains("unix"))
            return OperatingSystem.LINUX;

        if (operatingSystemName.contains("solaris") || operatingSystemName.contains("sunos"))
            return OperatingSystem.SOLARIS;

        return null;
    }

    public static ProcessorArchitecture getProcessorArchitecture() {
        switch (System.getProperty("os.arch")) {
            case "x86":
                return ProcessorArchitecture.X86;
            case "amd64":
            case "x86_64":
                return ProcessorArchitecture.X64;
            case "arm":
                return ProcessorArchitecture.ARM32;
            case "aarch64":
                return ProcessorArchitecture.ARM64;
            case "riscv64":
                return ProcessorArchitecture.RISCV64;
            case "ppc64le":
                return ProcessorArchitecture.PPC64LE;
        }

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

        if (operatingSystem == OperatingSystem.MACOSX)
            return Path.of(homeDirectory, "Library/Application Support");

        return Path.of(homeDirectory);
    }
}
