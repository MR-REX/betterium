package ru.mrrex.betterium.directories;

import java.io.IOException;

public class ClientInstanceNotFound extends IOException {

    public ClientInstanceNotFound(String clientId) {
        super("Client instance with the unique id \"%s\" was not found".formatted(clientId));
    }
}
