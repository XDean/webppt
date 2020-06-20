import React, {createContext, useEffect, useState} from 'react';
import {createStyles, makeStyles} from '@material-ui/core/styles';
import {useQuery} from "../util/util";
import {XElement} from "../model/model";
import {ParseError} from "../model/error";
import {renderElement} from "./render";
import {JElement} from "../model/json";
import {Parser} from "../model/parse";

const useStyles = makeStyles(theme => createStyles({}));

type SlideProp = {}

export type SlideContext = {
    sourceURL?: URL
    sourceContent?: string
    resourceURL?: URL
    rootElement?: XElement
}
const Context = createContext<SlideContext>({});

const SlideView: React.FunctionComponent<SlideProp> = (props) => {
    const query = useQuery();
    const path = query.get("path");
    const [error, setError] = useState("");
    const [ctx, setCtx] = useState<SlideContext>({});

    useEffect(() => {
        if (path) {
            const url = new URL(path);
            let resource;
            if (url.protocol.startsWith("http")) {
                resource = fetch(path);
            } else {
                resource = fetch(`resource?path=${path}`)
            }
            resource.then(r => {
                if (r.ok) {
                    return r.text()
                } else {
                    return r.text().then(t => {
                        throw t;
                    });
                }
            })
                .then(text => {
                    const root = Parser.parse(text);
                    setCtx({
                        rootElement: root,
                        sourceContent: text,
                        sourceURL: url,
                        resourceURL: url,
                    })
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

    if (ctx.rootElement) {
        return (
            <Context.Provider value={ctx}>
                <div>
                    {renderElement(ctx.rootElement)}
                </div>
            </Context.Provider>
        )
    }

    return (
        <div>
            Loading {path}
        </div>
    )
};

export default SlideView;