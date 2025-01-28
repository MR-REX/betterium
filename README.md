# Betterium

Betterium is a powerful Java library designed to streamline the creation and management of clients for the "Better Than Adventure!" game modification.
Whether you are building a custom launcher or need a reliable way to handle multiple client versions, Betterium provides a robust and flexible solution.

## Features

- **Client generation from JSON Configuration**:
    - Define your client setup using a JSON configuration file that specifies:
        - Libraries (e.g. `LWJGL`).
        - Native files (e.g. `.dll`, `.so` files for different platforms).
        - JAR files (e.g. game client JAR and modification JAR).

- **Automatic dependency management**:
    - Dependencies are automatically downloaded from Maven repository or retrieved from a local cache.
    - Ensures your client always has the required files without manual intervention.

- **Client directory handling**:
    - Generates a dedicated client directory for temporary files such as resource packs, translations, and screenshots.
    - Automatically cleans up and restores files to their original locations after the client is closed.

- **Easy client launching**:
    - Handles the entire process, from preparing dependencies to starting the client.

## Getting Started

### Prerequisites

1. Java 8 or higher.

### Usage

1. **Create a JSON Configuration file.** Define your client setup in a JSON file.

2. **Initialize Client instance.** Use Betterium to load dependencies, create the
client, and launch it.

```java
public class Main {
    public static void main(String[] args) {
        try {
            Path workingDirectoryPath = Paths.get(".betterium");
            Betterium betterium = new Betterium(workingDirectoryPath);

            Path clientConfigPath = Paths.get("client-config.json");
            ClientConfig clientConfig = betterium.loadClientConfig(clientConfigPath);

            ClientOptions clientOptions = clientConfig.getOptions();

            JvmArguments jvmArguments = new JvmArguments(clientOptions.getJvmArguments());
            JavaRuntime javaRuntime = new JavaRuntime(jvmArguments);

            ClientArguments clientArguments = new ClientArguments();
            clientArguments.setPlayerNickname("Player");
            clientArguments.setPlayerUuid(UUID.randomUUID());
            clientArguments.setGameDirectoryPath(workingDirectoryPath.resolve("temp"));

            Client client = betterium.createClient(clientConfig);
            int exitCode = client.run(javaRuntime, clientArguments);

            System.out.println(exitCode);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
```

## License

Betterium is licensed under the **MIT License**.