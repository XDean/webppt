import React, {useContext, useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement, XParam, XText} from "../../model/model";
import {renderChildren, renderElement, renderText} from "../render";
import PageView from "./page";
import KeyboardArrowLeftIcon from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@material-ui/icons/KeyboardArrowRight';
import {SlideContext} from "../../model/context";
import {useProperty} from "../../util/util";
import NavigatorView from "../tool/navigator";
import PageNumberView from "../tool/page-number";
import ToolbarView from "../tool/toolbar";

const useStyles = makeStyles({
    root: {
        position: "absolute",
        width: "100vw",
        height: "100vh",
        left: 0,
        top: 0,
        overflow: "hidden",
        boxSizing: "border-box",


        fontFamily: "'Times New Roman', sans-serif",
        fontSize: 26,
        textShadow: "0 1px 1px rgba(0, 0, 0, .1)",
        letterSpacing: -1,
        color: "#000",
        backgroundColor: "#fff",
    },
});

type RootProp = {
    element: XElement
}

const RootView: React.FunctionComponent<RootProp> = (props) => {
    const context = useContext(SlideContext);
    const classes = useStyles();

    const current = useProperty(context.state.currentPage);
    const pages = props.element.children.filter(n => n instanceof XElement && n.name == "page").map(n => n as XElement);
    context.state.totalPage.value = pages.length;

    useEffect(() => {
        document.addEventListener("keydown", event => {
            switch (event.keyCode) {
                case 37:// left arrow
                case 8: // backspace
                case 38:// up arrow
                // if (edit) {
                //     break;
                // }
                // fallthrough
                case 33:// PgDn
                    context.state.prevPage();
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
                    context.state.nextPage();
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
            <NavigatorView/>
            <PageNumberView/>
            <ToolbarView/>
        </Box>
    )
};

export default RootView;