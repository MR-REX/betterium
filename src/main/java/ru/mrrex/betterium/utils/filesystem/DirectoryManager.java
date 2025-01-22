package ru.mrrex.betterium.utils.filesystem;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DirectoryManager {

    public static void copyDirectory(Path sourceDirectoryPath, Path destinationDirectoryPath, CopyOption... copyOptions) throws IOException {
        Files.walk(sourceDirectoryPath).forEach(sourcePath -> {
            String relativePath = sourcePath.toString().substring(sourceDirectoryPath.toString().length());
            Path destinationPath = destinationDirectoryPath.resolve(relativePath);

            try {
                Files.copy(sourcePath, destinationPath, copyOptions);
            } catch (IOException exception) {
                throw new RuntimeException("Failed to copy object", exception);
            }
        });
    }

    public static void deleteDirectory(Path directoryPath) throws IOException {
        Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static Path defineDirectory(Path directoryPath) {
        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            return directoryPath;
        }

        try {
            Files.createDirectories(directoryPath);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to define directory", exception);
        }

        return directoryPath;
    }

    public static Path defineEmptyDirectory(Path directoryPath) {
        try {
            if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
                deleteDirectory(directoryPath);
            }

            Files.createDirectories(directoryPath);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to define empty directory", exception);
        }

        return directoryPath;
    }
}
