package ru.mrrex.betterium.entities.interfaces;

import java.net.URL;

public interface DownloadableEntity {

    URL getUrl();
    String getFileName();
    String getChecksum();
}
