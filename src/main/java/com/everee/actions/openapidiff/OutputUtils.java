package com.everee.actions.openapidiff;

import com.github.kjens93.actions.toolkit.core.Core;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;

public abstract class OutputUtils {

  public static void emitOutputs(ChangedOpenApi diff) {
    if (!diff.isDiffBackwardCompatible()) {
      Core.setOutput("classification", "major");
    } else if (diff.isDiff()) {
      Core.setOutput("classification", "minor");
    } else {
      Core.setOutput("classification", "patch");
    }
  }
}
