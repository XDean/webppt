import {XElement} from "./model";
import {createContext} from "react";
import {SimpleProperty} from "xdean-util";
import {TopicSocket} from "./socket";
import {resolveWebsocketURL} from "../util/util";

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

export class SlideContextData {
    static DEFAULT = new SlideContextData(new XElement(), "");

    readonly state = new State();
    readonly preference = new Preference();
    readonly ws = new TopicSocket(resolveWebsocketURL(new URL("socket/topic", window.location.href).href));

    constructor(
        readonly rootElement: XElement,
        readonly sourceContent: string,
        readonly resourceURL?: URL,
        readonly sourceURL?: URL,
    ) {
    }

    prevPage = () => {
        this.state.currentPage.update(c => c > 0 ? c - 1 : c);
    };
    nextPage = () => {
        this.state.currentPage.update(c => c < this.getPages().length - 1 ? c + 1 : c);
    };
    getPages = () => {
        return this.rootElement.children.filter(n => n instanceof XElement && n.name == "page").map(n => n as XElement);
    };
    gotoPage = (page: XElement | number) => {
        const pages = this.getPages();
        let index = -1;
        if (page instanceof XElement) {
            index = pages.indexOf(page);
        } else {
            index = page;
        }
        if (index >= 0 && index < pages.length) {
            this.state.currentPage.value = index;
        } else {
            console.warn("Invalid page to: ", page);
        }
    };
    resolveURL = (rel: string): URL => {
        if (this.resourceURL) {
            const url = new URL(rel, this.resourceURL);
            if (url.protocol.startsWith("http")) {
                return url;
            } else {
                return new URL(`api/resource?path=${url.href}`, this.preference.serverURL)
            }
        } else {
            return new URL(`api/resource?path=${rel}`, this.preference.serverURL)
        }
    };
    fetchText = (rel: string): Promise<string> => {
        return fetch(this.resolveURL(rel).href)
            .then(res => {
                if (res.ok) {
                    return res.text()
                } else {
                    return res.text().then(t => {
                        throw `Resource Server Error, [${res.status} ${res.statusText}]: ${t}`
                    })
                }
            })
            .catch(e => {
                throw `Fail to Fetch: ${e}`
            })
    }
}

export const SlideContext = createContext<SlideContextData>(SlideContextData.DEFAULT);
