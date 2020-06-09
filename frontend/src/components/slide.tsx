import React, {useEffect, useState} from 'react';
import {createStyles, makeStyles} from '@material-ui/core/styles';
import {useQuery} from "../util/util";
import {XElement} from "../model/model";
import {XError} from "../model/error";
import {renderElement} from "./render";
import {JElement} from "../model/json";

const useStyles = makeStyles(theme => createStyles({}));

type SlideProp = {}

const SlideView: React.FunctionComponent<SlideProp> = (props) => {
    const query = useQuery();
    const path = query.get("path");
    const [rootElement, setRootElement] = useState<XElement>();
    const [error, setError] = useState("");

    useEffect(() => {
        fetch(`/parse/?path=${path}`, {
            method: "GET",
        })
            .then(r => {
                if (r.ok) {
                    r.json().then(j => {
                        setRootElement(XElement.fromJson(j as JElement));
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

    if (rootElement) {
        return (
            <div>
                {renderElement(rootElement)}
            </div>
        )
    }

    return (
        <div>
            Loading {path}
        </div>
    )
};

export default SlideView;