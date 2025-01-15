package ru.mrrex.betterium.utils.hash;

public enum HashAlgorithm {
    
    SHA256("SHA-256");

    private final String name;

    HashAlgorithm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
