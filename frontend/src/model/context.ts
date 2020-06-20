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

export const SlideContext = createContext<SlideContextData>(SlideContextData.DEFAULT);
