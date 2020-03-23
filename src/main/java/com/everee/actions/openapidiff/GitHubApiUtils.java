package com.everee.actions.openapidiff;

import lombok.SneakyThrows;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.everee.actions.openapidiff.GitHubActionUtils.requireVariable;
import static com.everee.actions.openapidiff.GitHubActionUtils.requireVariableAsInt;

public abstract class GitHubApiUtils {

    public static GitHub github = initializeGitHub();
    public static GHRepository repository = initializeRepository();
    public static GHPullRequest pullRequest = initializePullRequest();

    @SneakyThrows
    public static GHPullRequest initializePullRequest() {
        var number = requireVariableAsInt("PULL_REQUEST");
        var repository = initializeRepository();
        return repository.getPullRequest(number);
    }

    @SneakyThrows
    public static GHRepository initializeRepository() {
        var repo = requireVariable("GITHUB_REPOSITORY");
        return github.getRepository(repo);
    }

    @SneakyThrows
    private static GitHub initializeGitHub() {
        var actor = requireVariable("GITHUB_ACTOR");
        var token = requireVariable("GITHUB_TOKEN");
        return GitHub.connect(actor, token);
    }
}
