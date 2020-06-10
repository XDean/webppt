import React from 'react';
import {createStyles, makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement} from "../../model/model";
import {renderChildren} from "../render";

const useStyles = makeStyles(theme => createStyles({
    page: {
        position: "absolute",
        width: "100vw",
        height: "100vh",
        left: 0,
        top: 0,

        color: "#000",
        backgroundColor: "#fff",

        transition: "transform .3s ease-out"
    }
}));

type PageProp = {
    element: XElement
}

const PageView: React.FunctionComponent<PageProp> = (props) => {
    const classes = useStyles();
    const pages = props.element.parent.children.filter(n => n.name == "page");
    const index = pages.indexOf(props.element);
    return (
        <Box className={classes.page}>
            <Box className="page-content">
                {renderChildren(props.element)}
            </Box>
            <Box className="page-footer">
                {index + 1} / {pages.length}
            </Box>
        </Box>
    )
};

export default PageView;