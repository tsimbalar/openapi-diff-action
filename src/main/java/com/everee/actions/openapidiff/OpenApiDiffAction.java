package com.everee.actions.openapidiff;

import com.qdesrame.openapi.diff.OpenApiCompare;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import com.qdesrame.openapi.diff.output.ConsoleRender;

import java.io.IOException;

import static com.everee.actions.openapidiff.GitHubActionUtils.printError;
import static com.everee.actions.openapidiff.GitHubActionUtils.printInfo;

class OpenApiDiffAction {

    private static final String summarySuccess = "Result: Compatible ✅";
    private static final String summaryFailure = "Result️: Incompatible️ ⚠️";
    private static final String descriptionSuccess = "The new OpenAPI spec is backward-compatible.";
    private static final String descriptionFailure = "The new OpenAPI spec contains changes that break backward-compatibility. Existing clients may crash or generate exceptions if this code is deployed to production.";

    public static void main(String... args) throws Exception {
        var diff = readDiff();
        writeToConsole(diff);
        writeToPullRequestLabel(diff);
        writeToPullRequestComment(diff);
        exitWithAppropriateStatusCode(diff);
    }

    private static ChangedOpenApi readDiff() {
        var HEAD_SPEC = GitHubActionUtils.requireVariable("HEAD_SPEC");
        printInfo("Reading HEAD spec from %s", HEAD_SPEC);
        var BASE_SPEC = GitHubActionUtils.requireVariable("BASE_SPEC");
        printInfo("Reading BASE spec from %s", BASE_SPEC);
        return OpenApiCompare.fromLocations(BASE_SPEC, HEAD_SPEC);
    }

    private static void writeToConsole(ChangedOpenApi diff) {
        printInfo(new ConsoleRender().render(diff));
    }

    private static void writeToPullRequestLabel(ChangedOpenApi diff) throws IOException {
        GitHubPullRequestUtils.addLabel(diff);
    }

    private static void writeToPullRequestComment(ChangedOpenApi diff) throws IOException {
        GitHubPullRequestUtils.addReport(diff);
    }

    private static void exitWithAppropriateStatusCode(ChangedOpenApi diff) {
        if (diff.isDiffBackwardCompatible()) {
            printInfo(String.join(" - ", summarySuccess, descriptionSuccess));
            System.exit(0);
        } else {
            printError(String.join(" - ", summaryFailure, descriptionFailure));
            System.exit(1);
        }
    }
}