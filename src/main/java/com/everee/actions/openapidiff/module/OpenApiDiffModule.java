package com.everee.actions.openapidiff.module;

import com.github.kjens93.actions.toolkit.core.Core;
import com.qdesrame.openapi.diff.OpenApiCompare;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import com.qdesrame.openapi.diff.output.ConsoleRender;
import com.qdesrame.openapi.diff.output.MarkdownRender;
import org.codejargon.feather.Provides;

import javax.inject.Singleton;

public class OpenApiDiffModule {

  @Provides
  @Singleton
  public ChangedOpenApi changedOpenApi() {
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

  @Provides
  @Singleton
  public ConsoleRender consoleRender() {
    return new ConsoleRender();
  }

  @Provides
  @Singleton
  public MarkdownRender markdownRender() {
    return new MarkdownRender();
  }
}
