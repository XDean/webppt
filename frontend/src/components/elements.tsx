import RootView from "./element/root";
import {XElement} from "../model/model";
import React, {FunctionComponent, ReactNode} from "react";

export interface ElementRender {
    name: string[]
    render: FunctionComponent<any>
}

export const ElementRenders: ElementRender[] = [
    {
        name: ["root"],
        render: RootView,
    },
];

export function renderElement(element: XElement): ReactNode | null {
    const render = ElementRenders.find(e => e.name.includes(element.name));
    if (render) {
        return <render.render element={element}/>
    } else {
        return <div>Unknown {element.name}</div>
    }
}

export function renderChildren(element: XElement): ReactNode {
    return (
        <React.Fragment>
            {element.children.map(e => {

            })}
        </React.Fragment>
    )
}