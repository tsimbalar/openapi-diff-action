package com.everee.actions.openapidiff.manager;

import com.github.kjens93.actions.toolkit.core.Core;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import com.qdesrame.openapi.diff.output.ConsoleRender;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.everee.actions.openapidiff.util.MessageUtils.getMessage;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ConsoleManager {

  private static final String summarySuccess = getMessage("console.summary.success");
  private static final String summaryFailure = getMessage("console.summary.failure");
  private static final String descriptionSuccess = getMessage("console.description.success");
  private static final String descriptionFailure = getMessage("console.description.failure");

  private final ConsoleRender consoleRender;
  private final ChangedOpenApi changedOpenApi;

  public void writeDiffToConsole() {
    Core.startGroup("OpenAPI Comparison Details");
    Core.info(consoleRender.render(changedOpenApi));
    Core.endGroup();
  }

  public void exitWithAppropriateStatusCode() {
    if (changedOpenApi.isDiffBackwardCompatible()) {
      Core.info(String.join(" - ", summarySuccess, descriptionSuccess));
    } else {
      Core.setFailed(String.join(" - ", summaryFailure, descriptionFailure));
    }
  }
}
