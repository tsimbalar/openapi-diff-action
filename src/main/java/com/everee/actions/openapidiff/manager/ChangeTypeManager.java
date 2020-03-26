package com.everee.actions.openapidiff.manager;

import com.everee.actions.openapidiff.model.ChangeType;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ChangeTypeManager {

  private final ChangedOpenApi changedOpenApi;

  public ChangeType getChangeType() {
    if (!changedOpenApi.isDiffBackwardCompatible()) {
      return ChangeType.MAJOR;
    } else if (changedOpenApi.isDiff()) {
      return ChangeType.MINOR;
    } else {
      return ChangeType.PATCH;
    }
  }
}
