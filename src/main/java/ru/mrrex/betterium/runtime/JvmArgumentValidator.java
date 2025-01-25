package ru.mrrex.betterium.runtime;

import java.util.regex.Pattern;

public class JvmArgumentValidator {

    private static final Pattern X_OPTION = Pattern.compile("-X[a-zA-Z0-9]+(=[a-zA-Z0-9]+)?"),
                                 XX_OPTION = Pattern.compile("-XX:[+\\-]?[a-zA-Z0-9]+(=[a-zA-Z0-9]+)?"),
                                 D_PROPERTY = Pattern.compile("-D[a-zA-Z0-9_.]+(=[^\\s]+)?");

    public static boolean isValidArgument(String argument) {
        if (argument == null || argument.isEmpty()) {
            return false;
        }

        return X_OPTION.matcher(argument).matches()
                || XX_OPTION.matcher(argument).matches()
                || D_PROPERTY.matcher(argument).matches();
    }
}
