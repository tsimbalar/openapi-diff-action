package com.everee.actions.openapidiff;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class GitHubActionUtils {

    public static void printInfo(String message, Object... parameters) {
        System.out.println(String.format(message, parameters));
    }

    public static void printError(String message, Object... parameters) {
        System.out.println("::error::" + String.format(message, parameters));
    }

    public static int requireVariableAsInt(String name) {
        return getVariableAsInt(name)
                .orElseThrow(() -> new IllegalStateException("Missing required input (must be an int): " + name));
    }

    public static String requireVariable(String name) {
        return getVariable(name)
                .orElseThrow(() -> new IllegalStateException("Missing required input: " + name));
    }

    private static Optional<Integer> getVariableAsInt(String name) {
        return getVariable(name)
                .filter(StringUtils::isNumeric)
                .map(Integer::parseInt);
    }

    public static boolean variableExists(String name) {
        return getVariable(name).isPresent();
    }

    private static Optional<String> getVariable(String name) {
        return Optional.ofNullable(name)
            .map(String::toUpperCase)
            .map(System::getenv)
        .filter(Predicate.not(String::isBlank));
    }
}
