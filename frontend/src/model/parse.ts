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
        let root = new XElement("root");
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
                    case '}':
                        this.parseEndTag();
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

    static multiLineElementStart = /^\.(\w+)\s*{\s*$/;
    private parseElement() {
        const last = this.getLastElement();
        const elem = new XElement();
        elem.parent = last;
        elem.raw.startLineIndex = this.index;
        last.children.push(elem);
        let multiLineMatcher = this.line.match(Parser.multiLineElementStart);
        if (multiLineMatcher){
            elem.name = multiLineMatcher[1];
            this.elemStack.push(elem);
        }
        int splitIndex = StringUtils.indexOfAny(line, "{ ");
        if (splitIndex == -1) {
            elem.setName(line.substring(1));
            elem.getRawInfo().setEndLineIndex(index);
        } else {
            elem.setName(line.substring(1, splitIndex));
            if (line.charAt(splitIndex) == ' ') {
                String args = line.substring(splitIndex + 1);
                parseSingleLineElement(elem, args);
            } else {
                if (!line.endsWith("{")) {
                    throw AppException.builder()
                        .line(index)
                        .message("multi line element start tag can't has content")
                        .build();
                }
                elemStack.addLast(elem);
            }
        }
        consumed = true;
    }

    private parseText() {

    }

    private parseEndTag() {

    }

    private static paramPattern = /^@(\w+)(@(\w*))?(=(.*))?$/;

    private parseParameterText(parent: XElement, line: string): XParam {
        const matcher = line.match(Parser.paramPattern);
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