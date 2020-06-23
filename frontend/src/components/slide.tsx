import React, {useEffect, useState} from 'react';
import {createStyles, makeStyles} from '@material-ui/core/styles';
import {useQuery} from "../util/util";
import {renderElement} from "./render";
import {Parser} from "../model/parse";
import {SlideContext, SlideContextData} from "../model/context";
import {Listener} from "xdean-util";
import {useHistory, useLocation} from "react-router";

const useStyles = makeStyles(theme => createStyles({}));

type SlideProp = {}


const SlideView: React.FunctionComponent<SlideProp> = (props) => {
    const query = useQuery();
    const path = query.get("path");
    const page = Number(query.get("page")) || 1;
    const [error, setError] = useState("");
    const [context, setContext] = useState<SlideContextData>(SlideContextData.DEFAULT);

    useEffect(() => {
        context.gotoPage(page - 1);
    }, [page]);

    const history = useHistory();
    const location = useLocation();
    useEffect(() => {
        const currentListener: Listener<number> = (p, o, n) => {
            const param = new URLSearchParams(location.search);
            param.set("page", (n + 1).toString());
            location.search = unescape(param.toString());
            history.push(location);
        };
        context.state.currentPage.addListener(currentListener);

        return () => {
            context.state.currentPage.removeListener(currentListener);
        };
    }, [context, location.search]);

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