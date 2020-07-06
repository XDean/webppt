import React, {useContext, useEffect, useRef} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box, RootRef} from "@material-ui/core";
import {XElement} from "../../model/model";
import PageView from "./page";
import {SlideContext, SlideContextData} from "../../model/context";
import {ifClass, useProperty} from "../../util/util";
import NavigatorView from "../tool/navigator";
import PageNumberView from "../tool/page-number";
import ToolbarView from "../tool/toolbar";
import useFullscreen from "../../util/fullscreen";
import {Listener} from "xdean-util";
import "./root.scss"

type RootProp = {
    element: XElement
}

const RootView: React.FunctionComponent<RootProp> = (props) => {
    const context = useContext(SlideContext);

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
            <Box className={"wp-root" + ifClass(presentMode === "outline", "outline")}>
                {context.pages.map((e, index) => {
                    return (
                        <PageView key={index} element={e.element} index={index}/>
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
        const edit = event.target instanceof HTMLTextAreaElement || (event.target instanceof HTMLElement && event.target.isContentEditable);
        switch (event.keyCode) {
            case 37:// left arrow
            case 8: // backspace
            case 38:// up arrow
                if (edit) {
                    break;
                }
            // fallthrough
            case 33:// PgDn
                context.prevPage();
                event.preventDefault();
                break;
            case 39:// right arrow
            case 32:// space
            case 40:// down arrow
                if (edit) {
                    break;
                }
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