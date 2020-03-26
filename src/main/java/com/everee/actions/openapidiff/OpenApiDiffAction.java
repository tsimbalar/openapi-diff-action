package com.everee.actions.openapidiff;

import com.everee.actions.openapidiff.manager.ConsoleManager;
import com.everee.actions.openapidiff.manager.OutputManager;
import com.everee.actions.openapidiff.manager.PullRequestCommentManager;
import com.everee.actions.openapidiff.manager.PullRequestLabelManager;
import com.everee.actions.openapidiff.module.GitHubModule;
import com.everee.actions.openapidiff.module.OpenApiDiffModule;
import lombok.RequiredArgsConstructor;
import org.codejargon.feather.Feather;

import javax.inject.Inject;
import java.io.IOException;

@RequiredArgsConstructor(onConstructor_ = @Inject)
class OpenApiDiffAction {

  private final OutputManager outputManager;
  private final ConsoleManager consoleManager;
  private final PullRequestLabelManager labelManager;
  private final PullRequestCommentManager commentManager;

  public static void main(String... args) throws Exception {
    var feather = Feather.with(new GitHubModule(), new OpenApiDiffModule());
    var action = feather.instance(OpenApiDiffAction.class);
    action.run();
  }

  public void run() throws IOException {
    labelManager.applyLabel();
    outputManager.emitOutputs();
    commentManager.applyComment();
    consoleManager.writeDiffToConsole();
    consoleManager.exitWithAppropriateStatusCode();
  }
}
