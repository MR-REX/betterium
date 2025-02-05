package ru.mrrex.betterium.entities.client;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ru.mrrex.betterium.utils.environment.Environment;

public class ClientArguments {

    private String playerNickname;
    private UUID playerUuid;

    private Path gameDirectoryPath;

    public ClientArguments() {
        playerNickname = "Player" + (System.currentTimeMillis() % 1000L);
        playerUuid = UUID.nameUUIDFromBytes(playerNickname.getBytes(StandardCharsets.UTF_8));

        gameDirectoryPath = Environment.getApplicationDirectoryPath().resolve(".minecraft-bta");
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setGameDirectoryPath(Path gameDirectoryPath) {
        this.gameDirectoryPath = gameDirectoryPath;
    }

    public Path getGameDirectoryPath() {
        return gameDirectoryPath;
    }

    public List<String> getArguments(String[] argumentsTemplate) {
        String uuid = playerUuid.toString();
        String gameDirectory = String.format("\"%s\"", gameDirectoryPath);

        return Arrays.stream(argumentsTemplate).map(argument -> {
            return argument.replace("%player_nickname%", playerNickname)
                           .replace("%player_uuid%", uuid)
                           .replace("%game_directory%", gameDirectory);
        }).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ClientArguments";
    }
}
