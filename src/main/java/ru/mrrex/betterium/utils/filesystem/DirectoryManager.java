package ru.mrrex.betterium.utils.filesystem;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class DirectoryManager {

    public static void moveDirectory(Path fromDirectoryPath, Path toDirectoryPath) throws IOException {
        if (!Files.exists(toDirectoryPath)) {
            Files.createDirectories(toDirectoryPath);
        }

        Files.walkFileTree(fromDirectoryPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path sourceFilePath, BasicFileAttributes attrs) throws IOException {
                Path targetFilePath = toDirectoryPath.resolve(fromDirectoryPath.relativize(sourceFilePath));
                Files.move(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path sourceDirectoryPath, BasicFileAttributes attrs) throws IOException {
                Path targetDirectoryPath = toDirectoryPath.resolve(fromDirectoryPath.relativize(sourceDirectoryPath));

                if (!Files.exists(targetDirectoryPath)) {
                    Files.createDirectories(targetDirectoryPath);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path sourceDirectoryPath, IOException exc) throws IOException {
                Files.delete(sourceDirectoryPath);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void copyDirectory(Path fromDirectoryPath, Path toDirectoryPath) throws IOException {
        if (!Files.exists(toDirectoryPath)) {
            Files.createDirectories(toDirectoryPath);
        }

        Files.walkFileTree(fromDirectoryPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path sourceFilePath, BasicFileAttributes attrs) throws IOException {
                Path targetFilePath = toDirectoryPath.resolve(fromDirectoryPath.relativize(sourceFilePath));
                Files.copy(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path sourceDirectoryPath, BasicFileAttributes attrs) throws IOException {
                Path targetDirectoryPath = toDirectoryPath.resolve(fromDirectoryPath.relativize(sourceDirectoryPath));

                if (!Files.exists(targetDirectoryPath)) {
                    Files.createDirectories(targetDirectoryPath);
                }

                return FileVisitResult.CONTINUE;
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
