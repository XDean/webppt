import {XElement} from "./model";
import {createContext} from "react";
import {SimpleProperty} from "xdean-util";
import {TopicSocket} from "./socket";
import {fetchText, resolveWebsocketURL} from "../util/util";

export class Preference {
    constructor(
        readonly serverURL: string = window.location.href,
    ) {
    }
}

export type PresentMode = "present" | "outline";

export class State {
    readonly currentPage = new SimpleProperty(0);

    readonly presentMode = new SimpleProperty<PresentMode>("present");
    readonly fullScreen = new SimpleProperty(false);
    readonly lockToolbar = new SimpleProperty(true);
}

export class PageContext {
    readonly animates = new SimpleProperty<XElement[]>([]);
    readonly currentAnimate = new SimpleProperty(0); // 0 to len

    constructor(
        readonly index: number,
        readonly element: XElement,
    ) {
    }

    prevAnim(): boolean {
        if (this.currentAnimate.value === 0) {
            return false;
        } else {
            this.currentAnimate.update(v => v - 1);
            return true;
        }
    }

    nextAnim() {
        if (this.currentAnimate.value === this.animates.value.length) {
            return false;
        } else {
            this.currentAnimate.update(v => v + 1);
            return true;
        }
    }
}

export class SlideContextData {
    static DEFAULT = new SlideContextData(new XElement());

    readonly state = new State();
    readonly preference = new Preference();
    readonly ws = new TopicSocket(resolveWebsocketURL(new URL("socket/topic", window.location.href).href));
    readonly pages: PageContext[];

    constructor(
        readonly rootElement: XElement,
        readonly sourceContent?: string,
        readonly resourceURL?: URL,
        readonly sourceURL?: URL,
    ) {
        let index = 0;
        this.pages = this.rootElement?.children
            .filter(n => n instanceof XElement && n.name === "page")
            .map(n => n as XElement)
            .map(e => new PageContext(index++, e));
    }

    prevPage = () => {
        if (!this.getCurrentPage().prevAnim()) {
            this.state.currentPage.update(c => c > 0 ? c - 1 : c);
            const currentPage = this.getCurrentPage();
            currentPage.currentAnimate.value = currentPage.animates.value.length;
        }
    };
    nextPage = () => {
        if (!this.getCurrentPage().nextAnim()) {
            this.state.currentPage.update(c => c < this.pages.length - 1 ? c + 1 : c);
            this.getCurrentPage().currentAnimate.value = 0;
        }
    };
    getMeta = () => {
        return this.rootElement!.children.filter(n => n instanceof XElement && n.name === "meta").map(n => n as XElement).shift();
    };
    getPage = (element: XElement): PageContext | undefined => {
        return this.pages.find(e => e.element.contains(element))
    };
    getCurrentPage = (): PageContext => {
        return this.pages[this.state.currentPage.value];
    };
    gotoPage = (page: XElement | number) => {
        let index = -1;
        if (page instanceof XElement) {
            const i = this.pages.find(e => e.element === page)?.index;
            index = i === undefined ? -1 : i;
        } else {
            index = page;
        }
        if (index >= 0 && index < this.pages.length) {
            this.state.currentPage.value = index;
        } else {
            console.warn("Invalid page to: ", page);
        }
    };
    resolveURL = (rel: string): URL => {
        if (this.resourceURL) {
            return new URL(rel, this.resourceURL);
        } else {
            return new URL(rel);
        }
    };
    resolveResourceURL = (rel: string): URL => {
        const url = this.resolveURL(rel);
        if (url.protocol.startsWith("http")) {
            return url;
        } else {
            return new URL(`api/resource?path=${url.href}`, this.preference.serverURL)
        }
    };
    fetchText = (rel: string): Promise<string> => {
        return fetchText(this.resolveResourceURL(rel))
    }
}

export const SlideContext = createContext<SlideContextData>(SlideContextData.DEFAULT);
