import {XNode} from "./model";

export class ParseError {
    constructor(
        readonly line: number,
        readonly message: string,
    ) {
    }
}

export class RenderError {
    constructor(
        readonly node: XNode,
        readonly message: string,
    ) {
    }
}