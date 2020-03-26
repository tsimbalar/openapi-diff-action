package com.everee.actions.openapidiff.manager;

import com.everee.actions.openapidiff.model.ChangeType;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RepositoryLabelManager {

  private final GHRepository repository;

  public GHLabel ensureLabel(ChangeType changeType) throws IOException {
    var name = changeType.labelName;
    var color = changeType.labelColor;
    var description = changeType.labelDescription;
    var existingLabel = repository.getLabel(name);
    if (existingLabel != null) {
      existingLabel.setColor(color);
      existingLabel.setDescription(description);
      return existingLabel;
    } else {
      return repository.createLabel(name, color, description);
    }
  }
}
