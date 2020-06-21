import {useLocation} from "react-router-dom";
import {Property} from "xdean-util";
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
        p.addListener((ob, o, n) => {
            setState(n.slice ? n.slice() : n);
        });
    }, [p]);
    return state
}

export function ifClass(condition: boolean, className: string): string {
    return condition ? " " + className : "";
}