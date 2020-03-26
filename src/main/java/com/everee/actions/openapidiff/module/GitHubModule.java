package com.everee.actions.openapidiff.module;

import com.github.kjens93.actions.toolkit.core.Core;
import org.codejargon.feather.Provides;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javax.inject.Singleton;
import java.io.FileReader;
import java.io.IOException;

public class GitHubModule {

  @Provides
  @Singleton
  public GitHub github() throws IOException {
    var token = Core.getInput("github-token", true);
    var actor = Core.getVariable("GITHUB_ACTOR", true);
    return org.kohsuke.github.GitHub.connect(actor, token);
  }

  @Provides
  @Singleton
  private GHRepository repository(GitHub github) throws IOException {
    var repositoryName = Core.getVariable("GITHUB_REPOSITORY", true);
    return github.getRepository(repositoryName);
  }

  @Provides
  @Singleton
  public GHPullRequest pullRequest(GitHub github, GHRepository repository) throws IOException {
    if (Core.getVariable("GITHUB_EVENT_NAME", true).equals("pull_request")) {
      var payloadPath = Core.getVariable("GITHUB_EVENT_PATH", true);
      try (var reader = new FileReader(payloadPath)) {
        var payload = github.parseEventPayload(reader, GHEventPayload.PullRequest.class);
        var number = payload.getNumber();
        return repository.getPullRequest(number);
      }
    } else {
      return null;
    }
  }
}
