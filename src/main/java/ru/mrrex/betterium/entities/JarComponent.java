package ru.mrrex.betterium.entities;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.mrrex.betterium.entities.interfaces.DownloadableEntity;

public class JarComponent implements DownloadableEntity {

    private URL url;

    @JsonProperty("sha256")
    private String hash;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "JarComponent [url=\"%s\"]".formatted(url);
    }
}
