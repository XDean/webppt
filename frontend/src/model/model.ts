import {JElement, JParam, JText} from "./json";

export type XRawInfo = {
    startLineIndex: number
    endLineIndex: number
}

export interface XNode {
    readonly name: string;
    parent: XElement;
    readonly raw: XRawInfo;

    getParam(key: string): XParam | null

    contains(node: XNode): boolean
}

export class XElement implements XNode {
    parent: XElement = this;
    readonly name: string;
    readonly children: XNode[] = [];
    readonly raw: XRawInfo;

    constructor(name: string, raw: XRawInfo) {
        this.name = name;
        this.raw = raw;
    }

    getParam(key: string): XParam | null {
        return this.getParamUntil(key, this);
    }

    getParamUntil(key: string, node: XNode): XParam | null {
        let param: XParam | null = null;
        for (const c of this.children) {
            if (c.contains(node)) {
                break;
            }
            if (!(c instanceof XParam)) {
                continue;
            }
            if (c.support(node) && c.key == key) {
                param = c;
            }
        }
        if (param) {
            return param
        } else if (this.parent == this) {
            return null;
        } else {
            return this.parent.getParamUntil(key, node);
        }
    }

    contains(node: XNode): boolean {
        return node == this || this.children.some(c => c.contains(node));
    }

    static fromJson(json: JElement): XElement {
        let element = new XElement(json.name, json.raw);
        let children = json.children.map(child => {
            if ((<JElement>child).name) {
                let e = XElement.fromJson(child as JElement);
                e.parent = element;
                return e;
            } else if ((<JText>child).lines) {
                return XText.fromJson(element, child as JText);
            } else {
                return XParam.fromJson(element, child as XParam);
            }
        });
        element.children.push(...children);
        return element;
    }
}

export class XParam implements XNode {
    parent: XElement;
    readonly name: string = "parameter";
    readonly key: string;
    readonly value: string;
    readonly element: string;
    readonly raw: XRawInfo;

    constructor(parent: XElement, key: string, value: string, element: string, raw: XRawInfo) {
        this.parent = parent;
        this.key = key;
        this.value = value;
        this.element = element;
        this.raw = raw;
    }

    contains(node: XNode): boolean {
        return node === this;
    }

    getParam(key: string): XParam | null {
        return this.parent.getParamUntil(key, this);
    }

    support(node: XNode): boolean {
        return this.element == "" || this.element == node.name;
    }

    static fromJson(parent: XElement, json: JParam): XParam {
        return new XParam(parent, json.key, json.value, json.element, json.raw);
    }
}

export class XText implements XNode {
    parent: XElement;
    readonly name: string = "text";
    readonly lines: string[] = [];
    readonly raw: XRawInfo;

    constructor(parent: XElement, lines: string[], raw: XRawInfo) {
        this.parent = parent;
        this.lines = lines;
        this.raw = raw;
    }

    contains(node: XNode): boolean {
        return node === this;
    }

    getParam(key: string): XParam | null {
        return this.parent.getParamUntil(key, this);
    }

    static fromJson(parent: XElement, json: JText): XText {
        return new XText(parent, json.lines, json.raw);
    }
}