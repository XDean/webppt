import React, {useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement, XParam, XText} from "../../model/model";
import {renderChildren, renderElement, renderText} from "../render";
import PageView from "./page";
import KeyboardArrowLeftIcon from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@material-ui/icons/KeyboardArrowRight';

const useStyles = makeStyles({
    root: {
        position: "absolute",
        width: "100vw",
        height: "100vh",
        left: 0,
        top: 0,
        overflow: "hidden",
        backgroundColor: "#cccccc",
        boxSizing: "border-box",
    },
    navigator: {
        position: "absolute",
        opacity: 0.7,
        textDecoration: "none",
        display: "flex",
        padding: "4px 6px",
        borderRadius: "50%",
        backgroundColor: "#ddd",
        color: "black",
        zIndex: 100,
        "&:hover": {
            backgroundColor: "#bbb",
            color: "black",
        }
    },
    navigatorLeft: {
        left: 5,
        bottom: 3,
    },
    navigatorRight: {
        left: 50,
        bottom: 3,
    },
    toolBar: {}
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
        console.log("effect")
        document.addEventListener("keydown", event => {
            console.log(event)
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
    }, []);

    return (
        <Box className={classes.root}>
            {pages.map((e, index) => {
                return (
                    <PageView key={index} element={e} index={index} total={pages.length} current={current}/>
                );
            })}

            <Box className={classes.navigator + " " + classes.navigatorLeft} onClick={prevPage}>
                <KeyboardArrowLeftIcon/>
            </Box>
            <Box className={classes.navigator + " " + classes.navigatorRight} onClick={nextPage}>
                <KeyboardArrowRightIcon/>
            </Box>
            <Box className={classes.toolBar}>
                <a id="page-outline-button" title="Outline"><i className="fa fa-th-large fa-fw"></i></a>
                <a id="page-fullscreen-button" title="Fullscreen"><i className="fa fa-arrows-alt fa-fw"></i></a>
                <a id="page-lock-button" title="Lock Toolbar"><i className="fa fa-lock fa-fw"></i></a>
            </Box>
        </Box>
    )
};

export default RootView;