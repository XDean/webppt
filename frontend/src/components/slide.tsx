import React, {useEffect, useState} from 'react';
import {renderElement} from "./render";
import {Parser} from "../model/parse";
import {SlideContext, SlideContextData} from "../model/context";
import {Listener} from "xdean-util";
import {XElement} from "../model/model";

type SlideProp = {}

const SlideView: React.FunctionComponent<SlideProp> = (props) => {
    const path = new URLSearchParams(window.location.search).get("path");
    const page = Number(window.location.hash.substring(1)) || 1;
    const [error, setError] = useState("");
    const [context, setContext] = useState<SlideContextData>(SlideContextData.DEFAULT);

    useEffect(() => {
        context.gotoPage(page - 1);
    }, [page]);

    useEffect(() => {
        const currentListener: Listener<number> = (p, o, n) => {
            window.location.hash = (n + 1).toString();
        };
        context.state.currentPage.addListener(currentListener);

        return () => {
            context.state.currentPage.removeListener(currentListener);
        };
    }, [context]);

    useEffect(() => {
        if (path) {
            const url = new URL(path);
            context.fetchText(path)
                .then(async text => {
                    const root = await Parser.parse(new SlideContextData(new XElement(), text, url, url));
                    let c = new SlideContextData(root, text, url, url);
                    c.state.currentPage.value = page - 1;
                    setContext(c);
                })
                .catch(e => {
                    setError(e);
                });
        }
    }, [path]);

    if (error) {
        return (
            <div>
                Error {JSON.stringify(error)}
            </div>
        )
    }

    if (context.rootElement) {
        return (
            <SlideContext.Provider value={context}>
                <div>
                    {renderElement(context.rootElement)}
                </div>
            </SlideContext.Provider>
        )
    }

    return (
        <div>
            Loading {path}
        </div>
    )
};

export default SlideView;