package ru.mrrex.betterium.entities.client;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ru.mrrex.betterium.entities.MavenArtifact;
import ru.mrrex.betterium.entities.RemoteFile;
import ru.mrrex.betterium.entities.interfaces.UniqueEntity;
import ru.mrrex.betterium.utils.hash.Hash;
import ru.mrrex.betterium.utils.hash.HashAlgorithm;

public class ClientConfig implements UniqueEntity {

    private String name;
    private String version;

    @JsonIgnore
    private String uniqueId;

    private RemoteFile[] components;
    private MavenArtifact[] dependencies;

    @JsonProperty("jvm_arguments")
    private Map<String, Object> jvmArguments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setComponents(RemoteFile[] components) {
        this.components = components;
    }

    public RemoteFile[] getComponents() {
        return components;
    }

    public MavenArtifact[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(MavenArtifact[] dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, Object> getJvmArguments() {
        return jvmArguments;
    }

    public void setJvmArguments(Map<String, Object> jvmArguments) {
        this.jvmArguments = jvmArguments;
    }

    public String getUniqueId() {
        if (uniqueId != null) {
            return uniqueId;
        }

        try {
            uniqueId = Hash.toHexString(Hash.getHash(name + version, HashAlgorithm.SHA256));
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException("Failed to get unique id of ClientInstance object", exception);
        };

        return uniqueId;
    }

    @Override
    public String toString() {
        return "ClientConfig [name=\"%s\", version=\"%s\", uid=\"%s\"]".formatted(
            name, version, getUniqueId()
        );
    }
}
