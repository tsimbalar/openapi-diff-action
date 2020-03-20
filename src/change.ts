import { DiffResult, DiffResultSpecEntityDetails, DiffResultType, DiffResultCode } from 'openapi-diff';
import * as diff from 'diff';
import * as yaml from 'yaml';

export class Change {
  readonly code: DiffResultCode;
  readonly type: DiffResultType;
  readonly diff: string;
  readonly location: string;

  constructor(result: DiffResult<any>) {
    this.code = result.code;
    this.type = result.type;
    this.diff = this.extractDiff(result);
    this.location = this.extractLocation(result);
  }

  private extractDiff(result: DiffResult<any>) {
    const srcLength = result.sourceSpecEntityDetails.length;
    const dstLength = result.destinationSpecEntityDetails.length;
    const length = Math.max(srcLength, dstLength);
    let diff = '';
    for (let i = 0; i < length; i++) {
      const src = result.sourceSpecEntityDetails[i];
      const dst = result.destinationSpecEntityDetails[i];
      diff += this.extractDiffChunk(src, dst) + '\n';
    }
    return diff;
  }

  private extractDiffChunk(src?: DiffResultSpecEntityDetails, dst?: DiffResultSpecEntityDetails) {
    const srcFilename = src?.location ?? '';
    const dstFilename = dst?.location ?? '';
    const srcString = yaml.stringify(src?.value ?? '');
    const dstString = yaml.stringify(dst?.value ?? '');
    return diff.createTwoFilesPatch(srcFilename, dstFilename, srcString, dstString);
  }

  private extractLocation(result: DiffResult<any>) {
    const srcLocation = result.sourceSpecEntityDetails?.[0]?.location;
    const dstLocation = result.destinationSpecEntityDetails?.[0]?.location;
    return srcLocation ?? dstLocation;
  }

  get description() {
    switch (this.code) {
      case 'path.add':
        return 'added new path';
      case 'method.add':
        return 'added new method';
      case 'request.body.scope.add':
        return 'added scope to request body';
      case 'response.body.scope.add':
        return 'added scope to response body';
      case 'response.optional.header.add':
        return 'added optional response header';
      case 'response.required.header.add':
        return 'added required response header';
      case 'response.status-code.add':
        return 'added status code to response';
      case 'unclassified.add':
        return 'added';
      case 'path.remove':
        return 'removed path';
      case 'method.remove':
        return 'removed method';
      case 'request.body.scope.remove':
        return 'removed scope from request body';
      case 'response.body.scope.remove':
        return 'removed scope from response body';
      case 'response.optional.header.remove':
        return 'removed optional response header';
      case 'response.required.header.remove':
        return 'removed required response header';
      case 'response.status-code.remove':
        return 'removed status code from response';
      case 'unclassified.remove':
        return 'removed';
      default:
        return this.code;
    }
  }

  get markdown() {
    return `\`${this.location}\`: ${this.description}\n<details>\n<summary>See diff</summary>\n\`\`\`diff\n${this.diff}\`\`\`</details>`;
  }
}
