package com.everee.actions.openapidiff;

import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public enum ChangeType {
    MAJOR("oas:major", "D73A4A", "Breaking changes to the OpenAPI spec"),
    MINOR("oas:minor", "008651", "Non-breaking changes to the OpenAPI spec"),
    PATCH("oas:patch", "E5E5E5", "No changes to the OpenAPI spec");

    public final String labelName;

    public final String labelColor;

    public final String labelDescription;

    public static Set<String> allLabelNames() {
        return Stream.of(values()).map(t -> t.labelName).collect(toSet());
    }

    public static String colorForLabel(String label) {
        return Stream.of(values())
                .filter(t -> Objects.equals(t.labelName, label))
                .map(t -> t.labelColor)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Label not supported: " + label));
    }
}
