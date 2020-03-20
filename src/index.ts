import * as fs from 'fs';
import * as core from '@actions/core';
import * as github from '@actions/github';

import { diffSpecs } from 'openapi-diff';
import { Change } from './change';

async function main() {
  const head = core.getInput('head', { required: true });
  const base = core.getInput('base', { required: true });
  const labelOnPR = core.getInput('label-on-pr');
  const commentOnPR = core.getInput('comment-on-pr');
  const warnOnDeprecatedRemoval = core.getInput('warn-on-deprecated-removal');

  core.info(`Reading HEAD spec from ${head}`);
  const headSpecString = fs.readFileSync(head).toString('utf8');

  core.info(`Reading BASE spec from ${base}`);
  const baseSpecString = fs.readFileSync(base).toString('utf8');

  core.info('Checking diff for breaking changes');
  const result = await diffSpecs({
    sourceSpec: {
      content: baseSpecString,
      location: base,
      format: 'openapi3',
    },
    destinationSpec: {
      content: headSpecString,
      location: head,
      format: 'openapi3',
    },
  });

  let breakingChanges: Change[] = [];
  let nonBreakingChanges: Change[] = [];
  let unclassifiedChanges: Change[] = [];

  if (result.breakingDifferencesFound) {
    for (const d of result.breakingDifferences) {
      const c = new Change(d);
      breakingChanges.push(c);
      core.error(`Breaking change detected:\n${c.description} (at ${c.location})\n${c.diff}`);
    }
  }

  if (result.nonBreakingDifferences.length > 0) {
    for (const d of result.nonBreakingDifferences) {
      const c = new Change(d);
      nonBreakingChanges.push(c);
      core.info(`Non-breaking change detected:\n${c.description} (at ${c.location})\n${c.diff}`);
    }
  }

  if (result.unclassifiedDifferences.length > 0) {
    for (const d of result.unclassifiedDifferences) {
      const c = new Change(d);
      unclassifiedChanges.push(c);
      core.info(`Unclassified change detected:\n$${c.description} (at ${c.location})\n${c.diff}`);
    }
  }

  if (result.breakingDifferencesFound) {
    core.setFailed('Breaking changes were found');
  }
}

main().catch(err => {
  console.error(err);
  core.setFailed(err.message);
});
