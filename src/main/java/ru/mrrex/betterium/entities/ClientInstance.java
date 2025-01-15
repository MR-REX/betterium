package ru.mrrex.betterium.entities;

import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.mrrex.betterium.utils.hash.Hash;
import ru.mrrex.betterium.utils.hash.HashAlgorithm;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInstance implements UniqueEntity {

    private String name;
    private String version;

    @JsonIgnore
    private String uniqueId;

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

    @Override
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
        return "ClientInstance [name=\"%s\", version=\"%s\", uid=\"%s\"]".formatted(
            name, version, getUniqueId()
        );
    }
}
