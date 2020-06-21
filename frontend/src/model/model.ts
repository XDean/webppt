import {JElement, JParam, JText} from "./json";

export class XRawInfo {
    public startLineIndex = -1;
    public endLineIndex = -1;
}

export interface XNode {
    readonly name: string;
    readonly parent: XElement;
    readonly raw: XRawInfo;

    getParam(key: string): XParam | null

    contains(node: XNode): boolean
}

export class XElement implements XNode {
    parent: XElement = this;
    name: string = '';
    children: XNode[] = [];
    raw: XRawInfo = new XRawInfo();

    getParam(key: string): XParam | null {
        return this.getParamUntil(key, this);
    }

    getBoolParam(key: string, def: boolean): boolean {
        const param = this.getParam(key);
        if (param) {
            return param.value === "true";
        } else {
            return def;
        }
    }


    getStrParam(key: string, def: string): string {
        const param = this.getParam(key);
        if (param) {
            return param.value;
        } else {
            return def;
        }
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

    getElements(): XElement[] {
        return this.children.filter(c => c instanceof XElement).map(c => c as XElement);
    }

    getParams(): XParam[] {
        return this.children.filter(c => c instanceof XParam).map(c => c as XParam);
    }

    getTexts(): XText[] {
        return this.children.filter(c => c instanceof XText).map(c => c as XText);
    }

    assertSingleTextLeaf(): XText {
        if (this.getElements().length != 0) {
            throw `${this.name} must has no child`;
        }
        let params = this.getParams();
        if (params[params.length - 1] != this.children[params.length - 1]) {
            throw `${this.name}'s param must defined first`
        }
        let texts = this.getTexts();
        if (texts.length != 1) {
            throw `${this.name} must has single text`
        }
        return texts[0]
    }
}

export class XParam implements XNode {
    readonly name: string = "parameter";
    raw: XRawInfo = new XRawInfo();

    constructor(
        public parent: XElement,
        readonly key: string,
        readonly value: string = "true",
        readonly element: string = "",
    ) {
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
}

export class XText implements XNode {
    readonly name: string = "text";
    readonly lines: string[] = [];
    raw: XRawInfo = new XRawInfo();

    constructor(
        public parent: XElement
    ) {
    }

    contains(node: XNode): boolean {
        return node === this;
    }

    getParam(key: string): XParam | null {
        return this.parent.getParamUntil(key, this);
    }
}