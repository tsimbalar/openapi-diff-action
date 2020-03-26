package com.everee.actions.openapidiff.manager;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PullRequestLabelManager {

  private final GHPullRequest pullRequest;
  private final ChangeTypeManager changeTypeManager;
  private final RepositoryLabelManager repositoryLabelManager;

  public void applyLabel() throws IOException {
    if (pullRequest == null) return;
    var changeType = changeTypeManager.getChangeType();
    var labelToAdd = repositoryLabelManager.ensureLabel(changeType);
    var labelsToRemove = getLabels(not(labelToAdd::equals));
    pullRequest.addLabels(labelToAdd);
    pullRequest.removeLabels(labelsToRemove);
  }

  private List<GHLabel> getLabels(Predicate<GHLabel> filter) throws IOException {
    return pullRequest.getLabels().stream()
        .filter(l -> l.getName().startsWith("oas:"))
        .filter(filter)
        .collect(Collectors.toList());
  }
}
