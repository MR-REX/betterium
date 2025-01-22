package ru.mrrex.betterium.entities;

import java.net.URL;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.mrrex.betterium.entities.interfaces.DownloadableEntity;

public class RemoteFile implements DownloadableEntity {

    @JsonProperty(required = true)
    private URL url;

    private String checksum;

    @JsonProperty("filename")
    private String fileName = null;

    @JsonProperty("global")
    private boolean global = false;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @JsonProperty("global")
    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean isGlobal) {
        this.global = isGlobal;
    }

    public String getFileName() {
        if (fileName != null && !fileName.isEmpty()) {
            return fileName;
        }

        return Path.of(url.getPath()).getFileName().toString();
    }

    @Override
    public String toString() {
        return "RemoteFile [url=\"%s\"]".formatted(url);
    }
}
