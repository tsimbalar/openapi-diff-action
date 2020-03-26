package com.everee.actions.openapidiff;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChangeType {
  MAJOR("oas:major", "D73A4A", "Breaking changes to the OpenAPI spec"),
  MINOR("oas:minor", "008651", "Non-breaking changes to the OpenAPI spec"),
  PATCH("oas:patch", "E5E5E5", "No changes to the OpenAPI spec");

  public final String labelName;

  public final String labelColor;

  public final String labelDescription;
}
