package ru.mrrex.betterium.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.mrrex.betterium.entities.interfaces.DownloadableEntity;
import ru.mrrex.betterium.utils.environment.OperatingSystem;
import ru.mrrex.betterium.utils.environment.ProcessorArchitecture;

public class MavenArtifact implements DownloadableEntity {

    @JsonProperty("group_id")
    private String groupId;
    
    @JsonProperty("artifact_id")
    private String artifactId;

    private String version;
    private String checksum;

    private Map<OperatingSystem, Map<ProcessorArchitecture, RemoteFile[]>> natives;

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

    public Map<OperatingSystem, Map<ProcessorArchitecture, RemoteFile[]>> getNatives() {
        return natives;
    }

    public void setNatives(Map<OperatingSystem, Map<ProcessorArchitecture, RemoteFile[]>> natives) {
        this.natives = natives;
    }

    public RemoteFile[] getNativeFiles(OperatingSystem operatingSystem, ProcessorArchitecture processorArchitecture) {
        if (natives == null || !natives.containsKey(operatingSystem)) {
            return null;
        }

        Map<ProcessorArchitecture, RemoteFile[]> processorArchitectureFiles = natives.get(operatingSystem);

        if (processorArchitectureFiles == null) {
            return null;
        }

        return processorArchitectureFiles.get(processorArchitecture);
    }

    public URL getUrl() {
        String remoteJarPath = String.format(
            "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.jar",
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

    public String getFileName() {
        return String.format("%s-%s.jar", artifactId, version);
    }

    public String getFullName() {
        return String.format("%s/%s/%s", groupId, artifactId, version);
    }

    @Override
    public String toString() {
        return String.format(
            "MavenArtifact [groupId=\"%s\", artifactId=\"%s\", version=\"%s\"]",
            groupId, artifactId, version
        );
    }
}
