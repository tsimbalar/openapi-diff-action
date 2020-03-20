import * as fs from "fs";
import * as diff from "openapi-diff";
import * as core from "@actions/core";
import * as github from "@actions/github";

async function main() {
  const labelOnPR = core.getInput("label-on-pr");
  const commentOnPR = core.getInput("comment-on-pr");
  const headSpecPath = core.getInput("head-spec-path");
  const baseSpecPath = core.getInput("base-spec-path");
  const warnOnDeprecatedRemoval = core.getInput("warn-on-deprecated-removal");
  const failOnBreakingDifferences = core.getInput(
    "fail-on-breaking-differences"
  );

  const headSpecString = fs.readFileSync(headSpecPath).toString("utf8");
  const baseSpecString = fs.readFileSync(baseSpecPath).toString("utf8");

  const result = await diff.diffSpecs({
    sourceSpec: {
      content: baseSpecString,
      location: baseSpecPath,
      format: "openapi3"
    },
    destinationSpec: {
      content: headSpecString,
      location: headSpecPath,
      format: "openapi3"
    }
  });

  let hasDifferences = false;
  let hasBreakingDifferences = false;
  let hasNonBreakingDifferences = false;

  if (result.breakingDifferences?.length ?? 0 > 0) {
    hasDifferences = true;
    hasBreakingDifferences = true;
    for (const d of result.breakingDifferences) {
      const srcLocation = d.sourceSpecEntityDetails?.[0]?.location;
      const dstLocation = d.destinationSpecEntityDetails?.[0]?.location;
      const location = srcLocation ?? dstLocation;
      console.log(`${d.code}: ${location}`);
    }
  }

  if (result.nonBreakingDifferences?.length ?? 0 > 0) {
    hasDifferences = true;
    hasNonBreakingDifferences = true;
    for (const d of result.nonBreakingDifferences) {
      const srcLocation = d.sourceSpecEntityDetails?.[0]?.location;
      const dstLocation = d.destinationSpecEntityDetails?.[0]?.location;
      const location = srcLocation ?? dstLocation;
      console.log(`${d.code}: ${location}`);
    }
  }

  if (hasBreakingDifferences && failOnBreakingDifferences) {
    core.setFailed("Breeaking difference(s) found in OpenAPI spec");
  }
}

main().catch(error => core.setFailed(error.message));
