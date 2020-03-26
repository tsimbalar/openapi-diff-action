package com.everee.actions.openapidiff;

class Action {

  public static void main(String... args) throws Exception {
    var diff = ReaderUtils.readDiff();
    OutputUtils.emitOutputs(diff);
    ConsoleUtils.writeToConsole(diff);
    PullRequestUtils.addLabelToPullRequest(diff);
    PullRequestUtils.addCommentToPullRequest(diff);
    ConsoleUtils.exitWithAppropriateStatusCode(diff);
  }
}
