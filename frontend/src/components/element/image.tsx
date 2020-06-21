import React, {useContext, useEffect} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box, Paper} from "@material-ui/core";
import {XElement} from "../../model/model";
import {SlideContext, resolveURL} from "../../model/context";

const useStyles = makeStyles({});

type ImageProp = {
    element: XElement
}

const ImageView: React.FunctionComponent<ImageProp> = (props) => {
    const ctx = useContext(SlideContext);
    const text = props.element.assertSingleTextLeaf();

    // const url = new URL(props.el)
    return (
        <React.Fragment>
            {text.lines.map((path, i) => {
                const url = resolveURL(ctx, path);
                if (url) {
                    return (
                        <div key={i}>
                            <img src={url.href}/>
                        </div>
                    );
                }
            })}
        </React.Fragment>
    )
};

export default ImageView;