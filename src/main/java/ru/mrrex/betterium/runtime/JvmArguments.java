package ru.mrrex.betterium.runtime;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JvmArguments {

    private final List<String> arguments;

    public JvmArguments(String[] arguments) {
        this.arguments = Arrays.asList(arguments);
    }

    public List<String> getValidArguments() {
        return arguments.stream()
                .filter(JvmArgumentValidator::isValidArgument)
                .collect(Collectors.toList());
    }

    public List<String> getInvalidArguments() {
        return arguments.stream()
                .filter(argument -> !JvmArgumentValidator.isValidArgument(argument))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "JvmArguments [count=%d]".formatted(arguments.size());
    }
}
