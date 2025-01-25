package ru.mrrex.betterium.entities.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientOptions {

    @JsonProperty("jvm_arguments")
    private String[] jvmArguments;

    @JsonProperty("client_arguments")
    private String[] clientArguments;

    @JsonProperty("use_legacy_directory")
    private boolean useLegacyDirectory;

    public String[] getJvmArguments() {
        return jvmArguments;
    }

    public void setJvmArguments(String[] jvmArguments) {
        this.jvmArguments = jvmArguments;
    }

    public String[] getClientArguments() {
        return clientArguments;
    }

    public void setClientArguments(String[] clientArguments) {
        this.clientArguments = clientArguments;
    }

    @JsonProperty("use_legacy_directory")
    public boolean shouldUseLegacyDirectory() {
        return useLegacyDirectory;
    }

    public void setUseLegacyDirectory(boolean useLegacyDirectory) {
        this.useLegacyDirectory = useLegacyDirectory;
    }

    @Override
    public String toString() {
        return "ClientOptions";
    }
}
