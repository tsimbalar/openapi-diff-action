package com.everee.actions.openapidiff;

import com.github.kjens93.actions.toolkit.core.Core;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import com.qdesrame.openapi.diff.output.ConsoleRender;

import static com.everee.actions.openapidiff.MessageUtils.getMessage;

public abstract class ConsoleUtils {

  private static final String summarySuccess = getMessage("console.summary.success");
  private static final String summaryFailure = getMessage("console.summary.failure");
  private static final String descriptionSuccess = getMessage("console.description.success");
  private static final String descriptionFailure = getMessage("console.description.failure");

  public static void writeToConsole(ChangedOpenApi diff) {
    Core.startGroup("OpenAPI Comparison Details");
    Core.info(new ConsoleRender().render(diff));
    Core.endGroup();
  }

  public static void exitWithAppropriateStatusCode(ChangedOpenApi diff) {
    if (diff.isDiffBackwardCompatible()) {
      Core.info(String.join(" - ", summarySuccess, descriptionSuccess));
    } else {
      Core.setFailed(String.join(" - ", summaryFailure, descriptionFailure));
    }
  }
}
