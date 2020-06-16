import React, {createContext, useEffect, useState} from 'react';
import {createStyles, makeStyles} from '@material-ui/core/styles';
import {useQuery} from "../util/util";
import {XElement} from "../model/model";
import {XError} from "../model/error";
import {renderElement} from "./render";
import {JElement} from "../model/json";

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
        fetch(`/parse/?path=${path}`, {
            method: "GET",
        })
            .then(r => {
                if (r.ok) {
                    r.json().then(j => {
                        setCtx({
                            rootElement: XElement.fromJson(j as JElement),
                        });
                    });
                } else {
                    r.json().then(j => {
                        setError((j as XError).message);
                    });
                }
            })
            .catch(e => {
                setError(e);
            });
    }, [path]);

    if (error) {
        return (
            <div>
                Error {error}
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