import React, {useContext} from 'react';
import {createStyles, makeStyles, Theme} from '@material-ui/core/styles';
import {Box, Paper} from "@material-ui/core";
import {XElement} from "../../model/model";
import {renderChildren} from "../render";
import {SlideContext} from "../../model/context";
import {useProperty} from "../../util/util";

const useStyles = makeStyles<Theme, PageProp>(theme => createStyles({
    wrapper: {
        transition: "transform .3s ease",
    },
    content: {
        position: "absolute",
        width: "100vw",
        height: "100vh",
        left: 0,
        top: 0,
        padding: "5vh 5vw",
        backgroundColor: "#fff",
        overflow: "auto",
        boxSizing: "border-box",
    },
    presentWrapper: {
        transform: props => {
            const offset = props.index - props.current;
            if (offset === 0) {
                return "translate(0)";
            } else if (offset === 1) {
                return "translate(100vw)";
            } else if (offset === -1) {
                return "translate(-100vw)";
            } else if (offset > 0) {
                return "translate(200vw)";
            } else {
                return "translate(-200vw)";
            }
        },
        zIndex: props => props.index - props.current === 0 ? 10 : 5,
    },
    outlineWrapper: {
        position: "relative",
        display: "inline-block",
        width: "20vw",
        height: "20vh",
        "&:hover": {
            boxShadow: "0 0 3px 4px #accaff",
            cursor: "pointer",
        }
    },
    outlineContent: {
        borderRadius: "2%",
        transition: "transform .3s ease-out",
        transform: "scale(0.2)",
        transformOrigin: "0 0",
    }
}));

type PageProp = {
    element: XElement
    index: number
    total: number
    current: number
}

const PageView: React.FunctionComponent<PageProp> = (props) => {
    const classes = useStyles(props);
    const context = useContext(SlideContext);
    const mode = useProperty(context.state.presentMode);
    return (
        <Box
            className={classes.wrapper + " " + (mode === "present" ? (classes.content + " " + classes.presentWrapper) : classes.outlineWrapper)}>
            {mode === "present" ? (
                renderChildren(props.element)
            ) : (
                <Paper className={classes.content + " " + classes.outlineContent} elevation={3}
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