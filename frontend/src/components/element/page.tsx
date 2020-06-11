import React from 'react';
import {createStyles, makeStyles, Theme} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement} from "../../model/model";
import {renderChildren} from "../render";

const useStyles = makeStyles<Theme, PageProp>(theme => createStyles({
    page: {
        position: "absolute",
        width: "100vw",
        height: "100vh",
        left: 0,
        top: 0,

        color: "#000",
        backgroundColor: "#fff",

        transition: "transform .3s ease-out",
        transform: props => {
            const offset = props.index - props.current;
            if (offset == 0) {
                return "translate(0)";
            } else if (offset > 0) {
                return "translate(100vw)";
            } else {
                return "translate(-100vw)";
            }
        },
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
    return (
        <Box className={classes.page}>
            <Box className="page-content">
                {renderChildren(props.element)}
            </Box>
            <Box className="page-footer">
                {props.index + 1} / {props.total}
            </Box>
        </Box>
    )
};

export default PageView;