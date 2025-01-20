package ru.mrrex.betterium.entities;

import java.net.URL;
import java.nio.file.Paths;

import ru.mrrex.betterium.entities.interfaces.DownloadableEntity;

public class RemoteFile implements DownloadableEntity {

    private URL url;
    private String checksum;

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

    public String getFileName() {
        return Paths.get(url.getPath()).getFileName().toString();
    }

    @Override
    public String toString() {
        return "RemoteFile [url=\"%s\"]".formatted(url);
    }
}
