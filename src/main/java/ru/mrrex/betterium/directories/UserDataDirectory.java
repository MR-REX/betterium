package ru.mrrex.betterium.directories;

import java.nio.file.Path;

import ru.mrrex.betterium.utils.filesystem.DirectoryManager;

public class UserDataDirectory {

    private final Path path;

    private final Path texturepacksDirectoryPath,
                       screenshotsDirectoryPath,
                       languagesDirectoryPath;

    public UserDataDirectory(Path path) {
        this.path = DirectoryManager.defineDirectory(path);

        this.texturepacksDirectoryPath = DirectoryManager.defineDirectory(path.resolve("texturepacks"));
        this.screenshotsDirectoryPath = DirectoryManager.defineDirectory(path.resolve("screenshots"));
        this.languagesDirectoryPath = DirectoryManager.defineDirectory(path.resolve("languages"));
    }

    public Path getPath() {
        return path;
    }

    public Path[] getInternalDirectoriesPaths() {
        return new Path[] {
            texturepacksDirectoryPath,
            screenshotsDirectoryPath,
            languagesDirectoryPath
        };
    }

    @Override
    public String toString() {
        return "UserDataDirectory [path=\"%s\"]".formatted(path);
    }
}
