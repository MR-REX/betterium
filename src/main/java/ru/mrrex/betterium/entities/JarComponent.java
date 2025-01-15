package ru.mrrex.betterium.entities;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JarComponent {

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
        return "ClientComponent [url=\"%s\"]".formatted(url);
    }
}
