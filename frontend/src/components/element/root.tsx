import React, {useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement, XParam, XText} from "../../model/model";
import {renderChildren, renderElement, renderText} from "../render";
import PageView from "./page";

const useStyles = makeStyles({
    root: {
        position: "absolute",
        width: "100vw",
        height: "100vh",
        left: 0,
        top: 0,
    }
});

type RootProp = {
    element: XElement
}

const RootView: React.FunctionComponent<RootProp> = (props) => {
    const pages = props.element.children.filter(n => n instanceof XElement && n.name == "page").map(n => n as XElement);

    const classes = useStyles();
    const [current, setCurrent] = useState(0);

    const prevPage = () => setCurrent(c => c > 0 ? c - 1 : c);
    const nextPage = () => setCurrent(c => c < pages.length - 1 ? c + 1 : c);

    useEffect(() => {
        document.addEventListener("keypress", event => {
            switch (event.keyCode) {
                case 37:// left arrow
                case 8: // backspace
                case 38:// up arrow
                // if (edit) {
                //     break;
                // }
                // fallthrough
                case 33:// PgDn
                    prevPage();
                    event.preventDefault();
                    break;
                case 39:// right arrow
                case 32:// space
                case 40:// down arrow
                // if (edit) {
                //     break;
                // }
                // fallthrough
                case 34:// PgDn
                    nextPage();
                    event.preventDefault();
                    break;
            }
        });
    });

    return (
        <Box className={classes.root}>
            {pages.map((e, index) => {
                return (
                    <PageView key={index} element={e} index={index} total={pages.length} current={current}/>
                );
            })}
        </Box>
    )
};

export default RootView;