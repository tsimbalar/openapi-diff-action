package com.everee.actions.openapidiff.manager;

import com.github.kjens93.actions.toolkit.core.Core;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class OutputManager {

  private final ChangeTypeManager changeTypeManager;

  public void emitOutputs() {
    switch (changeTypeManager.getChangeType()) {
      case MAJOR:
        Core.setOutput("classification", "major");
      case MINOR:
        Core.setOutput("classification", "minor");
      case PATCH:
        Core.setOutput("classification", "patch");
    }
  }
}
