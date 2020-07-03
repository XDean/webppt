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
    total: number
    current: number
}

const PageView: React.FunctionComponent<PageProp> = (props) => {
    const context = useContext(SlideContext);
    const mode = useProperty(context.state.presentMode);
    const offset = Math.min(2, Math.max(-2, props.index - props.current));
    return (
        <Box
            className={`wp-page-wrapper ${mode}`}>
            {mode === "present" ? (
                <Box className={`content offset-${offset}`}>
                    {renderChildren(props.element)}
                </Box>
            ) : (
                <Paper className={"content"} elevation={3}
                       onClick={() => {
                           context.gotoPage(props.element);
                           context.state.presentMode.value = "present";
                       }}>
                    {renderChildren(props.element)}
                </Paper>
            )}
        </Box>
    );
};

export default PageView;