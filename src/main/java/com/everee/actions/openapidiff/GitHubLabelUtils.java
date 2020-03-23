package com.everee.actions.openapidiff;

import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public abstract class GitHubLabelUtils {

    public static void applyOasLabel(GHPullRequest pullRequest, ChangeType changeType) throws IOException {
        removeExistingOasLabels(pullRequest, not(changeType.labelName::equals));
        createOrUpdateOasLabel(pullRequest.getRepository(), changeType);
        pullRequest.addLabels(changeType.labelName);
    }

    private static void createOrUpdateOasLabel(GHRepository repository, ChangeType changeType) throws IOException {
        var existingLabel = repository.getLabel(changeType.labelName);
        if (existingLabel != null) {
            existingLabel.setColor(changeType.labelColor);
            existingLabel.setDescription(changeType.labelDescription);
        } else {
            repository.createLabel(changeType.labelName, changeType.labelColor, changeType.labelDescription);
        }
    }

    private static void removeExistingOasLabels(GHPullRequest pullRequest, Predicate<String> filter) throws IOException {
        var labelsToRemove = getExistingOasLabels(pullRequest, filter);
        pullRequest.removeLabels(labelsToRemove);
    }

    public static List<GHLabel> getExistingOasLabels(GHPullRequest pullRequest, Predicate<String> filter) throws IOException {
        return pullRequest.getLabels().stream()
                .filter(l -> l.getName().startsWith("oas:"))
                .filter(l -> filter.test(l.getName()))
                .collect(Collectors.toList());
    }
}
