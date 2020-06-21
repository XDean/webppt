import {XElement} from "./model";
import {createContext} from "react";

export class Preference {
    static DEFAULT = new Preference(window.location.href);

    constructor(
        readonly serverURL: string,
    ) {
    }
}

export class SlideContextData {
    static DEFAULT = new SlideContextData(new XElement(), "", Preference.DEFAULT);

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
