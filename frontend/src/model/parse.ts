import {XElement, XNode, XParam, XText} from "./model";
import {ParseError} from "./error";
import {arrayRemove, fetchText} from "../util/util";
import {SlideContextData} from "./context";

export class Parser {
    lines: string[];
    index: number = -1;
    line: string = "";
    consumed: boolean = false;
    elemStack: XNode[] = [];

    constructor(private context: SlideContextData) {
        this.lines = (context.sourceContent || "").split("\n");
        let root = new XElement();
        root.name = "root";
        root.raw.startLineIndex = 0;
        this.elemStack.push(root);
    }

    static parse(context: SlideContextData): Promise<XElement> {
        return new Parser(context).parse();
    }

    async parse(): Promise<XElement> {
        while (this.nextLine()) {
            if (this.line.length != 0) {
                switch (this.line.charAt(0)) {
                    case '.':
                        this.parseElement();
                        break;
                    case '@':
                        await this.parseParameter();
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
        while (this.elemStack.length > 1) {
            const node = this.elemStack.slice(-1)[0];
            if (node instanceof XText) {
                this.elemStack.pop();
                continue;
            }
            throw new ParseError(node.raw.startLineIndex, `unclosed tag: ${node.name}`);
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
        throw new ParseError(this.index, "getLastElement error never happen");
    }

    private async parseParameter() {
        const paramPattern = /^@(\w+)(?:@(\w*))?(?:\s*=\s*(.*))?$/;
        const matcher = this.line.match(paramPattern);
        if (!matcher) {
            throw new ParseError(this.index, "Invalid parameter: " + this.line);
        }
        const parent = this.getLastElement();
        const key = matcher[1];
        const element = matcher[2] || "";
        const value = matcher[3] || "true";
        const parameter = new XParam(parent, key, value, element);
        parameter.raw.startLineIndex = this.index;
        parameter.raw.endLineIndex = this.index;
        parent.children.push(parameter);
        await this.handleIncludeCommand(parameter);
        this.consumed = true;
    }

    private parseElement() {
        const multiLineElementEnd = /^\.}\s*$/;
        if (this.line.match(multiLineElementEnd)) {
            while (true) {
                if (this.elemStack.length == 1) {
                    throw new ParseError(this.index, "no element to close");
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
        const singleLineElement = /^\.(\w+)((?:\s+@\w+(?:=(?:"(?:[^"\\]|\\.)*"|\S+))?)*)(?:\s+(.*))?$/;
        const singleLineElementParam = /(?:\s+@(\w+)(?:=("(?:[^"\\]|\\.)*"|\S+))?)/g;

        let singleLineMatcher = this.line.match(singleLineElement);
        if (singleLineMatcher) {
            elem.name = singleLineMatcher[1];
            elem.raw.endLineIndex = this.index;
            if (singleLineMatcher[2]) {
                while (true) {
                    const matcher = singleLineElementParam.exec(singleLineMatcher[2]);
                    if (!matcher) {
                        break;
                    }
                    console.log(matcher);
                    const param = new XParam(elem, matcher[1], matcher[2]);
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

        throw new ParseError(this.index, "unrecognized element grammar");
    }

    private async handleIncludeCommand(param: XParam) {
        if (param.key !== "include") {
            return;
        }
        let url, text;
        if (param.value.startsWith("@")) {
            url = new URL(`api/template?name=${param.value.substring(1)}`, this.context.preference.serverURL);
            text = await fetchText(url)
        } else {
            url = this.context.resolveURL(param.value);
            text = await this.context.fetchText(param.value);
        }
        const includeElement = await Parser.parse(new SlideContextData(new XElement(), text, url, url));
        const index = param.parent.children.indexOf(param);
        if (index != -1) {
            param.parent.children.splice(index, 1, ...includeElement.children);
            includeElement.children.forEach(c => c.parent = param.parent);
        }
    }
}