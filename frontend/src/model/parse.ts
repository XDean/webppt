import {XElement, XNode, XParam, XText} from "./model";
import {XError} from "./error";
import {arrayRemove} from "../util/util";

export class Parser {
    lines: string[];
    index: number = -1;
    line: string = "";
    consumed: boolean = false;
    elemStack: XNode[] = [];

    constructor(source: string) {
        this.lines = source.split(/\R/);
        let root = new XElement();
        root.name = "root";
        root.raw.startLineIndex = 0;
        this.elemStack.push(root);
    }

    parse(): XElement | undefined {
        while (this.nextLine()) {
            if (this.line.length != 0) {
                switch (this.line.charAt(0)) {
                    case '.':
                        this.parseElement();
                        break;
                    case '@':
                        this.parseParameter();
                        break;
                    case '/':
                        if (this.line.startsWith("//")) { // comment
                            this.consumed = true;
                        }
                        break;
                    case '\\':
                        this.line = this.line.substring(1);
                        break;
                }
            }
            if (!this.consumed) {
                if (this.elemStack.length == 1) {
                    throw new XError(this.index, "text can't define as top node");
                } else {
                    const node = this.elemStack.slice(-1)[0];
                    if (node instanceof XText) {
                        node.lines.push(this.line);
                        node.raw.endLineIndex = this.index;
                    } else if (node instanceof XElement) {
                        const newText = new XText(node);
                        newText.lines.push(this.line);
                        newText.raw.startLineIndex = this.index;
                        newText.raw.endLineIndex = this.index;
                        node.children.push(newText);
                        this.elemStack.push(newText);
                    }
                }
            }
        }
        if (this.elemStack.length > 1) {
            const startLine = this.elemStack.slice(-1)[0].raw.startLineIndex;
            throw new XError(startLine, "unclosed tag");
        }
        return this.elemStack[0] as XElement
    }

    private nextLine(): boolean {
        if (this.index == this.lines.length - 1) {
            return false;
        }
        this.index++;
        this.line = this.lines[this.index];
        this.consumed = false;
        return true;
    }

    private getLastElement(): XElement {
        const last = this.elemStack.slice(-1)[0];
        if (last instanceof XText) {
            this.elemStack.pop();
            if (last.lines.every(line => line.trim().length == 0)) {
                arrayRemove(last.parent.children, last);
            }
            return this.getLastElement();
        } else if (last instanceof XElement) {
            return last;
        }
        throw new XError(this.index, "getLastElement error never happen");
    }

    private parseParameter() {
        const last = this.getLastElement();
        const parameter = this.parseParameterText(last, this.line);
        last.children.push(parameter);
        this.consumed = true;
    }

    private parseElement() {
        const multiLineElementEnd = /^\.}\s*$/;
        if (this.line.match(multiLineElementEnd)) {
            while (true) {
                if (this.elemStack.length == 1) {
                    throw new XError(this.index, "no element to close");
                }
                const node = this.elemStack.pop();
                if (node instanceof XElement) {
                    node.raw.endLineIndex = this.index;
                    break;
                }
            }
            this.consumed = true;
            return;
        }

        const last = this.getLastElement();
        const elem = new XElement();
        elem.parent = last;
        elem.raw.startLineIndex = this.index;
        last.children.push(elem);

        // .<name> {
        // group 1: name
        const multiLineElementStart = /^\.(\w+)\s*{\s*$/;

        let multiLineMatcher = this.line.match(multiLineElementStart);
        if (multiLineMatcher) {
            elem.name = multiLineMatcher[1];
            this.elemStack.push(elem);
            this.consumed = true;
            return;
        }

        // .<name> [<param>*] <text>
        // group 1: name
        // group 2: params
        // group 3: text
        const singleLineElement = /^\.(\w+)((?:\s+@\w+(?:=(?:\w+|"(?:[^"\\]|\\.)*"))?)*)(?:\s+(.*))?$/;
        const singleLineElementParam = /(?:\s+@(\w+)(?:=(\w+|"(?:[^"\\]|\\.)*"))?)/g;

        let singleLineMatcher = this.line.match(singleLineElement);
        if (singleLineMatcher) {
            elem.name = singleLineMatcher[1];
            elem.raw.endLineIndex = this.index;
            if (singleLineMatcher[2]) {
                for (let group in singleLineMatcher[2].matchAll(singleLineElementParam)) {
                    const param = new XParam(elem, group[1], group[2]);
                    param.raw.startLineIndex = this.index;
                    param.raw.endLineIndex = this.index;
                    elem.children.push(param);
                }
            }
            if (singleLineMatcher[3]) {
                const text = new XText(elem);
                text.lines.push(singleLineMatcher[3]);
                text.raw.startLineIndex = this.index;
                text.raw.endLineIndex = this.index;
                elem.children.push(text);
            }
            this.consumed = true;
            return;
        }

        throw new XError(this.index, "unrecognized element grammar");
    }


    private parseParameterText(parent: XElement, line: string): XParam {
        const paramPattern = /^@(\w+)(@(\w*))?(=(.*))?$/;
        const matcher = line.match(paramPattern);
        if (!matcher) {
            throw new XError(this.index, "Invalid parameter: " + line);
        }
        const key = matcher[1];
        const element = matcher[3] || "";
        const value = matcher[5] || "true";
        const parameter = new XParam(parent, key, value, element);
        parameter.raw.startLineIndex = this.index;
        parameter.raw.endLineIndex = this.index;
        return parameter;
    }
}