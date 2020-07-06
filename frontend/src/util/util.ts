import {useLocation} from "react-router-dom";
import {Listener, Property} from "xdean-util";
import {useEffect, useState} from "react";

export function useQuery() {
    return new URLSearchParams(useLocation().search);
}

export function arrayRemove<T>(array: Array<T>, value: T) {
    let index = array.indexOf(value);
    if (index !== -1) {
        array.splice(index);
    }
}

export function useProperty<S extends (any | any[])>(p: Property<S>): S {
    let [state, setState] = useState<S>(() => p.value);
    useEffect(() => {
        setState(p.value);
        const listener: Listener<S> = (ob, o, n) => {
            setState(n.slice ? n.slice() : n);
        };
        p.addListener(listener);
        return () => p.removeListener(listener);
    }, [p]);
    return state
}

export function ifClass(condition: boolean, className: string): string {
    return condition ? " " + className : "";
}

export function getExtension(path: string): string {
    return path.split('.').pop() || ""
}

export function resolveWebsocketURL(url: string): string {
    if (url.startsWith("http://")) {
        url = "ws://" + url.substring(7);
    }
    if (url.startsWith("https://")) {
        url = "wss://" + url.substring(8);
    }
    return url;
}

export function fetchText(url: URL): Promise<string> {
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
}