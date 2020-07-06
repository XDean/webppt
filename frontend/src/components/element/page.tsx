import React, {useContext} from 'react';
import {Box, Paper} from "@material-ui/core";
import {XElement} from "../../model/model";
import {renderChildren} from "../render";
import {SlideContext} from "../../model/context";
import {useProperty} from "../../util/util";
import "./page.scss"

type PageProp = {
    element: XElement
    index: number
}

const PageView: React.FunctionComponent<PageProp> = (props) => {
    const context = useContext(SlideContext);
    const mode = useProperty(context.state.presentMode);
    const current = useProperty(context.state.currentPage);
    const offset = Math.min(2, Math.max(-2, props.index - current));
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