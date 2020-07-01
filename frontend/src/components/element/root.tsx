import React, {useContext, useEffect, useRef, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box, RootRef} from "@material-ui/core";
import {XElement, XParam, XText} from "../../model/model";
import {renderChildren, renderElement, renderText} from "../render";
import PageView from "./page";
import KeyboardArrowLeftIcon from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@material-ui/icons/KeyboardArrowRight';
import {SlideContext, SlideContextData} from "../../model/context";
import {ifClass, useProperty, useQuery} from "../../util/util";
import NavigatorView from "../tool/navigator";
import PageNumberView from "../tool/page-number";
import ToolbarView from "../tool/toolbar";
import useFullscreen from "../../util/fullscreen";
import {Listener} from "xdean-util";
import {useHistory} from "react-router";
import {useLocation} from "react-router-dom";

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
        backgroundColor: "#ddd",
    },
    outline: {
        overflowY: "auto",
        display: "grid",
        gridTemplateColumns: "repeat(4,auto)",
        alignItems: "center",
        justifyItems: "center",
        gridRowGap: 15,
        gridColumnGap: 20,
        padding: "80px 50px",
    }
});

type RootProp = {
    element: XElement
}

const RootView: React.FunctionComponent<RootProp> = (props) => {
    const context = useContext(SlideContext);
    const classes = useStyles();

    const current = useProperty(context.state.currentPage);
    const pages = context.getPages();

    const rootRef = useRef(null);
    const [fullscreen, setFullScreen] = useFullscreen(rootRef);

    const presentMode = useProperty(context.state.presentMode);

    useEffect(() => {
        const fullListener: Listener<boolean> = (p, o, n) => {
            setFullScreen(n);
        };
        context.state.fullScreen.addListener(fullListener);
        return () => {
            context.state.fullScreen.removeListener(fullListener);
        };
    }, [context]);

    useEffect(() => {
        bindKeyEvent(context);
        handleMeta(context);
    }, [context]);

    return (
        <RootRef rootRef={rootRef}>
            <Box className={classes.root + ifClass(presentMode === "outline", classes.outline)}>
                {pages.map((e, index) => {
                    return (
                        <PageView key={index} element={e} index={index} total={pages.length} current={current}/>
                    );
                })}
                {presentMode === "present" && <NavigatorView/>}
                {presentMode === "present" && <PageNumberView/>}
                <ToolbarView/>
            </Box>
        </RootRef>
    )
};

function bindKeyEvent(context: SlideContextData) {
    let listener = (event: KeyboardEvent) => {
        switch (event.keyCode) {
            case 37:// left arrow
            case 8: // backspace
            case 38:// up arrow
            // if (edit) {
            //     break;
            // }
            // fallthrough
            case 33:// PgDn
                context.prevPage();
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
                context.nextPage();
                event.preventDefault();
                break;
        }
    };
    document.addEventListener("keydown", listener);
    return () => document.removeEventListener("keydown", listener);
}

function handleMeta(context: SlideContextData) {
    const meta = context.getMeta();
    if (!meta) {
        return;
    }
    const icon = meta.getStrParam("icon", "");
    if (icon) {
        const link = (document.querySelector("link[rel*='icon']") || document.createElement('link')) as HTMLLinkElement;
        link.type = 'image/x-icon';
        link.rel = 'shortcut icon';
        link.href = context.resolveResourceURL(icon).href;
        document.head.appendChild(link);
    }
    const title = meta.getStrParam("title", "");
    if (title) {
        document.title = title;
    }
}

export default RootView;