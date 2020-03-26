package com.everee.actions.openapidiff;

import com.github.kjens93.actions.toolkit.core.Core;
import com.qdesrame.openapi.diff.OpenApiCompare;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;

public abstract class ReaderUtils {

  public static ChangedOpenApi readDiff() {
    Core.startGroup("Reading OpenAPI Specs");
    var headSpec = Core.getInput("head-spec", true);
    var baseSpec = Core.getInput("base-spec", true);
    Core.info("Reading HEAD spec from " + headSpec);
    Core.info("Reading BASE spec from " + baseSpec);
    Core.info("Performing Diff");
    var diff = OpenApiCompare.fromLocations(baseSpec, headSpec);
    Core.endGroup();
    return diff;
  }
}
