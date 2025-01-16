package ru.mrrex.betterium.entities;

import java.net.URL;

import ru.mrrex.betterium.entities.interfaces.DownloadableEntity;

public class JarComponent implements DownloadableEntity {

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

    @Override
    public String toString() {
        return "JarComponent [url=\"%s\"]".formatted(url);
    }
}
