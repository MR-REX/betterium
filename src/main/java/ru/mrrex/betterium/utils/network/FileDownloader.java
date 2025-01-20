package ru.mrrex.betterium.utils.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownloader {

    public static final int MAX_RETRIES = 3;

    public static void downloadFile(URL url, File file) throws IOException {
        File parentDirectory = file.getParentFile();

        if (parentDirectory == null || !parentDirectory.isDirectory() || !parentDirectory.exists()) {
            throw new IOException("File does not have a parent directory");
        }

        try (InputStream inputStream = url.openStream();
             ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }

    public static void downloadFileWithRetries(URL url, File file, int maxRetries) throws IOException, RetryLimitExceededException {
        for (int retry = 0; retry < maxRetries; retry++) {
            try {
                downloadFile(url, file);
            } catch (IOException exception) {
                if (retry == maxRetries - 1)
                    throw new RetryLimitExceededException("Failed to download file", exception);

                throw exception;
            }
        }
    }

    public static void downloadFileWithRetries(URL url, File file) throws IOException, RetryLimitExceededException {
        downloadFileWithRetries(url, file, MAX_RETRIES);
    }
}
