package ru.mrrex.betterium.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader {

    public static void download(URL url, File file) throws IOException {
        try (InputStream inputStream = url.openStream();
             ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }
}
