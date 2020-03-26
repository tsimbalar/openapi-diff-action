package com.everee.actions.openapidiff.manager;

import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import com.qdesrame.openapi.diff.output.MarkdownRender;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHPullRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.stream.StreamSupport;

import static com.everee.actions.openapidiff.util.MessageUtils.getMessage;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PullRequestCommentManager {

  private static final String title = getMessage("pr.comment.title");
  private static final String tagline = getMessage("pr.comment.tagline");
  private static final String identifier = getMessage("pr.comment.identifier");
  private static final String shieldSuccess = getMessage("pr.comment.shield.success");
  private static final String shieldFailure = getMessage("pr.comment.shield.failure");
  private static final String summarySuccess = getMessage("pr.comment.summary.success");
  private static final String summaryFailure = getMessage("pr.comment.summary.failure");
  private static final String descriptionSuccess = getMessage("pr.comment.description.success");
  private static final String descriptionFailure = getMessage("pr.comment.description.failure");

  private final GHPullRequest pullRequest;
  private final ChangedOpenApi changedOpenApi;
  private final MarkdownRender markdownRender;

  public void applyComment() throws IOException {
    if (pullRequest == null) return;
    var newCommentBody = createCommentBody();
    var existingComment = findExistingComment();
    if (existingComment != null) {
      existingComment.update(newCommentBody);
    } else {
      pullRequest.comment(newCommentBody);
    }
  }

  private String createCommentBody() {
    var shield = createShield();
    var summary = createSummary();
    var markdown = markdownRender.render(changedOpenApi);
    var description = createDescription();
    return String.join(
        "\n", identifier, title, shield, summary, description, markdown, "---", tagline);
  }

  private String createShield() {
    return changedOpenApi.isDiffBackwardCompatible() ? shieldSuccess : shieldFailure;
  }

  private String createSummary() {
    return changedOpenApi.isDiffBackwardCompatible() ? summarySuccess : summaryFailure;
  }

  private String createDescription() {
    return changedOpenApi.isDiffBackwardCompatible() ? descriptionSuccess : descriptionFailure;
  }

  private GHIssueComment findExistingComment() throws IOException {
    var spliterator = pullRequest.listComments().withPageSize(20).spliterator();
    return StreamSupport.stream(spliterator, false)
        .filter(c -> c.getBody().contains(identifier))
        .findFirst()
        .orElse(null);
  }
}
