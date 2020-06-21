import RootView from "./element/root";
import {XElement, XText} from "../model/model";
import React, {FunctionComponent, Key, ReactNode} from "react";
import PlainTextView from "./text/plain";
import {Pageview} from "@material-ui/icons";
import PageView from "./element/page";
import MarkdownView from "./text/md";
import HTMLView from "./text/html";
import HomeView from "./element/home";
import ImageView from "./element/image";
import CodeView from "./element/code";

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
    },
    {
        name: ["home", "homepage"],
        render: HomeView,
    },
    {
        name: ["image", "img"],
        render: ImageView,
    },
    {
        name: ["code"],
        render: CodeView,
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

export function renderElement(element: XElement, key?: Key): ReactNode | null {
    const render = ElementRenders.find(e => e.name.includes(element.name));
    if (render) {
        return <render.render key={key} element={element}/>
    } else {
        return <div key={key}>Unknown {element.name}</div>
    }
}

export function renderText(text: XText, key?: Key): ReactNode {
    const type = text.getParam("type")?.value || 'md';
    const render = TextRenders.find(e => (type && e.name.includes(type)));
    if (render) {
        return <render.render key={key} text={text}/>
    } else {
        return <div key={key}>Unknown {text.name}</div>
    }
}

export function renderChildren(element: XElement): ReactNode {
    return (
        <React.Fragment>
            {element.children.map((e, i) => {
                if (e instanceof XElement) {
                    return renderElement(e, i);
                } else if (e instanceof XText) {
                    return renderText(e, i);
                } else {
                    return null;
                }
            })}
        </React.Fragment>
    )
}