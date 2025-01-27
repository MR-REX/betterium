package ru.mrrex.betterium.directories;

import java.io.IOException;

public class ClientInstanceNotFound extends IOException {

    public ClientInstanceNotFound(String clientId) {
        super(String.format("Client instance with the unique id \"%s\" was not found", clientId));
    }
}
