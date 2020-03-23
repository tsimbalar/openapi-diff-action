package com.everee.actions.openapidiff;

import com.qdesrame.openapi.diff.OpenApiCompare;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import com.qdesrame.openapi.diff.output.ConsoleRender;
import com.qdesrame.openapi.diff.output.MarkdownRender;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.HttpConnector;

import java.io.IOException;
import java.util.Objects;

import static java.lang.System.getenv;
import static java.util.Objects.requireNonNull;

class OpenApiDiffAction {

    public static void main(String... args) throws Exception {
        var diff = readDiff();
        writeToConsole(diff);
        writeToPullRequestComment(diff);
        exitWithAppropriateStatusCode(diff);
    }

    private static ChangedOpenApi readDiff() {
        var HEAD_SPEC = requireVariable("OPENAPI_HEAD_SPEC");
        printInfo("Reading HEAD spec from %s", HEAD_SPEC);
        var BASE_SPEC = requireVariable("OPENAPI_BASE_SPEC");
        printInfo("Reading BASE spec from %s", BASE_SPEC);
        return OpenApiCompare.fromLocations(BASE_SPEC, HEAD_SPEC);
    }

    private static void writeToConsole(ChangedOpenApi diff) {
        printInfo(new ConsoleRender().render(diff));
    }

    private static void writeToPullRequestComment(ChangedOpenApi diff) throws IOException {
        var github = initializeGitHub();
        var markdown = new MarkdownRender().render(diff);
        var repository = github.getRepository(requireVariable("GITHUB_REPOSITORY"));
        var pullRequestNumber = optionalIntVariable("PULL_REQUEST_ID", -1);
        if (pullRequestNumber == -1) {
            printInfo("No pull request number provided for this execution; skipping comment");
        } else {
            var pullRequest = repository.getPullRequest(pullRequestNumber);
            pullRequest.comment(markdown);
            printInfo("Added comment to pull request " + pullRequestNumber);
        }
    }

    private static void exitWithAppropriateStatusCode(ChangedOpenApi diff) {
        if (diff.isDiffBackwardCompatible()) {
            printInfo("OpenAPI spec is backward-compatible ✅");
            System.exit(0);
        } else {
            printError("OpenAPI spec contains breaking changes ❌");
            System.exit(1);
        }
    }

    private static GitHub initializeGitHub() throws IOException {
        return new GitHubBuilder()
                .withOAuthToken(requireVariable("GITHUB_TOKEN"), requireVariable("GITHUB_ACTOR"))
                .withConnector(initializeGitHubHttpConnector())
                .build();
    }

    private static HttpConnector initializeGitHubHttpConnector() {
        var HTTP_CONNECTOR = optionalVariable("HTTP_CONNECTOR");
        var isOffline = Objects.equals(HTTP_CONNECTOR, "OFFLINE");
        return isOffline ? HttpConnector.OFFLINE : HttpConnector.DEFAULT;
    }

    private static int optionalIntVariable(String name, int defaultValue) {
        var stringValue = optionalVariable(name);
        if (stringValue == null || stringValue.isBlank()) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(stringValue);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Cannot parse integer from environment variable: " + name);
            }
        }
    }

    private static String requireVariable(String name) {
        return requireNonNull(getenv(name), "Missing required environment variable: " + name);
    }

    private static String optionalVariable(String name) {
        return getenv(name);
    }

    private static void printInfo(String message, Object... parameters) {
        System.out.println(String.format(message, parameters));
    }

    private static void printError(String message, Object... parameters) {
        System.out.println("::error::" + String.format(message, parameters));
    }
}