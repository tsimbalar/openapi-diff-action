package com.everee.actions.openapidiff;

import com.github.kjens93.actions.toolkit.core.Core;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.util.Optional;

public abstract class GitHubUtils {

  public static final GitHub github = initializeGitHub();

  public static final GHRepository repository = initializeRepository();

  public static final GHPullRequest pullRequest = initializePullRequest();

  @SneakyThrows
  private static GitHub initializeGitHub() {
    var actor = Core.getVariable("GITHUB_ACTOR", true);
    var token = Core.getVariable("GITHUB_TOKEN", true);
    return GitHub.connect(actor, token);
  }

  @SneakyThrows
  private static GHRepository initializeRepository() {
    var repo = Core.getVariable("GITHUB_REPOSITORY", true);
    return github.getRepository(repo);
  }

  @SneakyThrows
  private static GHPullRequest initializePullRequest() {
    return Optional.ofNullable(Core.getInput("pull-request"))
        .filter(StringUtils::isNumeric)
        .map(Integer::parseInt)
        .map(GitHubUtils::getPullRequest)
        .orElse(null);
  }

  @SneakyThrows
  private static GHPullRequest getPullRequest(int number) {
    return repository.getPullRequest(number);
  }
}
