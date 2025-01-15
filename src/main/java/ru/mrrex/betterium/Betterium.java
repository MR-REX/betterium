package ru.mrrex.betterium;

import java.nio.file.Path;

import ru.mrrex.betterium.utils.Environment;

public class Betterium {

    private final WorkingDirectory workingDirectory;

    public Betterium(Path workingDirectoryPath) {
        workingDirectory = new WorkingDirectory(workingDirectoryPath);
    }

    public Betterium() {
        Path workingDirectoryPath = Environment.getApplicationDirectoryPath().resolve(".betterium");
        workingDirectory = new WorkingDirectory(workingDirectoryPath);
    }
}
