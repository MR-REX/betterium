package ru.mrrex.betterium.utils.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessorArchitecture {

    X64,
    X86,
    ARM64,
    ARM32,
    RISCV64,
    PPC64LE;

    @JsonCreator
    public static ProcessorArchitecture fromValue(String value) {
        return ProcessorArchitecture.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
