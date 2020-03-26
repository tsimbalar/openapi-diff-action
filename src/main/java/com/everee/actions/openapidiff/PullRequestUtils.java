package com.everee.actions.openapidiff;

import com.github.kjens93.actions.toolkit.core.Core;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import com.qdesrame.openapi.diff.output.MarkdownRender;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHPullRequest;

import java.io.IOException;
import java.util.stream.StreamSupport;

import static com.everee.actions.openapidiff.MessageUtils.getMessage;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class PullRequestUtils {

  private static final String title = getMessage("pr.comment.title");
  private static final String tagline = getMessage("pr.comment.tagline");
  private static final String identifier = getMessage("pr.comment.identifier");
  private static final String shieldSuccess = getMessage("pr.comment.shield.success");
  private static final String shieldFailure = getMessage("pr.comment.shield.failure");
  private static final String summarySuccess = getMessage("pr.comment.summary.success");
  private static final String summaryFailure = getMessage("pr.comment.summary.failure");
  private static final String descriptionSuccess = getMessage("pr.comment.description.success");
  private static final String descriptionFailure = getMessage("pr.comment.description.failure");

  public static void addLabelToPullRequest(ChangedOpenApi diff) throws IOException {
    Core.startGroup("Adding label to pull request");
    try {
      if (isNotBlank(Core.getInput("pull-request"))) {
        if (Core.getInput("add-label-to-pull-request", true).equals("true")) {
          var changeType = ChangeTypeUtils.getChangeType(diff);
          var pullRequest = GitHubUtils.pullRequest;
          var pullRequestNumber = pullRequest.getNumber();
          var changeTypeLabelName = changeType.labelName;
          GitHubLabelUtils.applyOasLabel(pullRequest, changeType);
          Core.info(getMessage("pr.output.label.applied", changeTypeLabelName, pullRequestNumber));
        } else {
          Core.info(getMessage("pr.output.flag.skipping.label"));
        }
      } else {
        Core.info(getMessage("pr.output.notfound.skipping.label"));
      }
    } finally {
      Core.endGroup();
    }
  }

  public static void addCommentToPullRequest(ChangedOpenApi diff) throws IOException {
    Core.startGroup("Adding comment to pull request");
    try {
      if (isNotBlank(Core.getInput("pull-request"))) {
        if (Core.getInput("add-comment-to-pull-request", true).equals("true")) {
          var commentBody = createCommentBody(diff);
          var pullRequest = GitHubUtils.pullRequest;
          var existingComment = findExistingComment(pullRequest);
          if (existingComment != null) {
            existingComment.update(commentBody);
            var commentId = existingComment.getId();
            var pullRequestNumber = pullRequest.getNumber();
            Core.info(getMessage("pr.output.comment.created", commentId, pullRequestNumber));
          } else {
            var newComment = pullRequest.comment(commentBody);
            var commentId = newComment.getId();
            var pullRequestNumber = pullRequest.getNumber();
            Core.info(getMessage("pr.output.comment.updated", commentId, pullRequestNumber));
          }
        } else {
          Core.info(getMessage("pr.output.flag.skipping.comment"));
        }
      } else {
        Core.info(getMessage("pr.output.notfound.skipping.comment"));
      }
    } finally {
      Core.endGroup();
    }
  }

  private static String createCommentBody(ChangedOpenApi diff) {
    var markdown = new MarkdownRender().render(diff);
    var shield = createShield(diff);
    var summary = createSummary(diff);
    var description = createDescription(diff);
    return String.join(
        "\n", identifier, title, shield, summary, description, markdown, "---", tagline);
  }

  private static String createShield(ChangedOpenApi diff) {
    return diff.isDiffBackwardCompatible() ? shieldSuccess : shieldFailure;
  }

  private static String createSummary(ChangedOpenApi diff) {
    return diff.isDiffBackwardCompatible() ? summarySuccess : summaryFailure;
  }

  private static String createDescription(ChangedOpenApi diff) {
    return diff.isDiffBackwardCompatible() ? descriptionSuccess : descriptionFailure;
  }

  private static GHIssueComment findExistingComment(GHPullRequest pullRequest) throws IOException {
    var spliterator = pullRequest.listComments().withPageSize(20).spliterator();
    return StreamSupport.stream(spliterator, false)
        .filter(c -> c.getBody().contains(identifier))
        .findFirst()
        .orElse(null);
  }
}
