package ru.mrrex.betterium.utils.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OperatingSystem {
    
    WINDOWS,
    MACOSX,
    LINUX,
    SOLARIS;

    @JsonCreator
    public static OperatingSystem fromValue(String value) {
        return OperatingSystem.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
