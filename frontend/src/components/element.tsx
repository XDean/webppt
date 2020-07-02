import React from 'react';
import {Box} from "@material-ui/core";
import {XElement} from "../model/model";
import AnimateView from "./animate/animate";

type ElementProp = {
    element: XElement
}

const ElementView: React.FunctionComponent<ElementProp> = (props) => {
    let children = props.children;
    const animate = props.element.getParam("animate");
    if (animate) {
        children = (
            <AnimateView element={props.element}>
                {children}
            </AnimateView>
        )
    }
    return (
        <React.Fragment>
            {children}
        </React.Fragment>
    )
};

export default ElementView;