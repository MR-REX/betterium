package ru.mrrex.betterium.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.mrrex.betterium.entities.interfaces.DownloadableEntity;
import ru.mrrex.betterium.entities.interfaces.StorableEntity;

public class MavenArtifact implements DownloadableEntity, StorableEntity {

    @JsonProperty("group_id")
    private String groupId;
    
    @JsonProperty("artifact_id")
    private String artifactId;

    private String version;
    private String checksum;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public URL getUrl() {
        String remoteJarPath = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.jar".formatted(
            groupId.replace('.', '/'),
            artifactId,
            version,
            artifactId,
            version
        );

        try {
            return new URL(remoteJarPath);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Failed to generate link to .jar file from the Maven repository", exception);
        }
    }

    @Override
    public Path getRelativePath() {
        return Path.of(groupId, artifactId + "-" + version + ".jar");
    }

    @Override
    public String toString() {
        return "MavenArtifact [groupId=\"%s\", artifactId=\"%s\", version=\"%s\"]".formatted(
            groupId, artifactId, version
        );
    }
}
