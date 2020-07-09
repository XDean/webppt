import React, {useContext} from 'react';
import {Box, Paper} from "@material-ui/core";
import {XElement} from "../../model/model";
import {renderChildren} from "../render";
import {SlideContext} from "../../model/context";
import {useProperty, usePropertyMap} from "../../util/util";
import "./page.scss"

type PageProp = {
    element: XElement
    index: number
}

const PageView: React.FunctionComponent<PageProp> = (props) => {
    const context = useContext(SlideContext);
    const mode = useProperty(context.state.presentMode);
    const offset = usePropertyMap(context.state.currentPage, p => Math.min(1, Math.max(-1, props.index - p)));
    const children = renderChildren(props.element);
    return (
        <Box className={`wp-page-wrapper ${mode}`}>
            <Paper className={`content offset-${offset}`} elevation={mode === "outline" ? 3 : 0}
                   onClick={mode === "outline" ? () => {
                       context.gotoPage(props.element);
                       context.state.presentMode.value = "present";
                   } : undefined}>
                {children}
            </Paper>
        </Box>
    );
};

export default PageView;