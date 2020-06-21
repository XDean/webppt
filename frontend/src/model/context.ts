import {XElement} from "./model";
import {createContext} from "react";
import {SimpleProperty} from "xdean-util";

export class Preference {
    static DEFAULT = new Preference(window.location.href);

    constructor(
        readonly serverURL: string,
    ) {
    }
}

export type PresentMode = "present" | "outline";

export class State {
    readonly totalPage = new SimpleProperty(0);
    readonly currentPage = new SimpleProperty(0);

    readonly presentMode = new SimpleProperty<PresentMode>("present");
    readonly fullScreen = new SimpleProperty(false);
    readonly lockToolbar = new SimpleProperty(true);

    prevPage = () => {
        this.currentPage.update(c => c > 0 ? c - 1 : c);
    };
    nextPage = () => {
        this.currentPage.update(c => c < this.totalPage.value - 1 ? c + 1 : c);
    };
}

export class SlideContextData {
    static DEFAULT = new SlideContextData(new XElement(), "", Preference.DEFAULT);

    readonly state = new State();

    constructor(
        readonly rootElement: XElement,
        readonly sourceContent: string,
        readonly preference: Preference,
        readonly resourceURL?: URL,
        readonly sourceURL?: URL,
    ) {
    }
}

export function resolveURL(ctx: SlideContextData, rel: string): URL | null {
    if (ctx.resourceURL) {
        const url = new URL(rel, ctx.resourceURL);
        if (url.protocol.startsWith("http")) {
            return url;
        } else {
            return new URL(`resource?path=${url.href}`, ctx.preference.serverURL)
        }
    } else {
        return null
    }
}

export function fetchText(ctx: SlideContextData, rel: string): Promise<string> {
    const url = resolveURL(ctx, rel);
    if (url) {
        return fetch(url.href)
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
    } else {
        return Promise.reject("Resource path is not specified");
    }
}

export const SlideContext = createContext<SlideContextData>(SlideContextData.DEFAULT);
