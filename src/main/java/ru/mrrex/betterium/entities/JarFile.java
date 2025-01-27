package ru.mrrex.betterium.entities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class JarFile {

    private Map<String, byte[]> entries;

    public JarFile(Map<String, byte[]> entries) {
        this.entries = entries;
    }

    public JarFile() {
        this(new LinkedHashMap<>());
    }

    public Map<String, byte[]> getEntries() {
        return entries;
    }

    public void append(JarFile jarFile) {
        entries.putAll(jarFile.entries);
    }

    public void write(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file); JarOutputStream jos = new JarOutputStream(fos)) {
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                jos.putNextEntry(new JarEntry(entry.getKey()));
                jos.write(entry.getValue());
                jos.closeEntry();
            }
        }
    }

    @Override
    public String toString() {
        return String.format("JarFile [entries=%d]", entries.size());
    }

    public static JarFile fromFile(File file) throws FileNotFoundException, IOException {
        Map<String, byte[]> entries = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(file); JarInputStream jis = new JarInputStream(fis)) {
            JarEntry entry;

            while ((entry = jis.getNextJarEntry()) != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                byte[] data = new byte[4096];
                int bytesReaded;

                while ((bytesReaded = jis.read(data)) != -1) {
                    buffer.write(data, 0, bytesReaded);
                }

                entries.put(entry.getName(), buffer.toByteArray());
            }
        }

        return new JarFile(entries);
    }
}
