package ru.mrrex.betterium.directories;

import java.nio.file.Path;

import ru.mrrex.betterium.utils.filesystem.DirectoryManager;

public class UserDataDirectory {

    private final Path path;

    private final Path texturepacksDirectoryPath,
                       screenshotsDirectoryPath;

    public UserDataDirectory(Path path) {
        this.path = DirectoryManager.defineDirectory(path);

        this.texturepacksDirectoryPath = DirectoryManager.defineDirectory(path.resolve("texturepacks"));
        this.screenshotsDirectoryPath = DirectoryManager.defineDirectory(path.resolve("screenshots"));
    }

    public Path getPath() {
        return path;
    }

    public Path getTexturepacksDirectoryPath() {
        return texturepacksDirectoryPath;
    }

    public Path getScreenshotsDirectoryPath() {
        return screenshotsDirectoryPath;
    }

    @Override
    public String toString() {
        return "UserDataDirectory [path=\"%s\"]".formatted(path);
    }
}
