import RootView from "./element/root";
import {XElement, XText} from "../model/model";
import React, {FunctionComponent, ReactNode} from "react";
import PlainTextView from "./text/plain";
import {Pageview} from "@material-ui/icons";
import PageView from "./element/page";
import MarkdownView from "./text/md";
import HTMLView from "./text/html";

export interface Render {
    name: string[]
    render: FunctionComponent<any>
}

export const ElementRenders: Render[] = [
    {
        name: ["root"],
        render: RootView,
    },
    {
        name: ["page"],
        render: PageView,
    }
];

export const TextRenders: Render[] = [
    {
        name: ["plain"],
        render: PlainTextView,
    },
    {
        name: ["md", "markdown"],
        render: MarkdownView,
    },
    {
        name: ["html"],
        render: HTMLView,
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

export function renderText(text: XText): ReactNode {
    const type = text.getParam("type")?.value || 'md';
    const render = TextRenders.find(e => (type && e.name.includes(type)));
    if (render) {
        return <render.render text={text}/>
    } else {
        return <div>Unknown {text.name}</div>
    }
}

export function renderChildren(element: XElement): ReactNode {
    return (
        <React.Fragment>
            {element.children.map(e => {
                if (e instanceof XElement) {
                    return renderElement(e);
                } else if (e instanceof XText) {
                    return renderText(e);
                } else {
                    return null;
                }
            })}
        </React.Fragment>
    )
}