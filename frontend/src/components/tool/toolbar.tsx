import React, {useContext} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box, IconButton, Paper, Tooltip} from "@material-ui/core";
import NavigatorView from "./navigator";
import ViewModuleIcon from '@material-ui/icons/ViewModule';
import FullscreenIcon from '@material-ui/icons/Fullscreen';
import FullscreenExitIcon from '@material-ui/icons/FullscreenExit';
import LockOpenIcon from '@material-ui/icons/LockOpen';
import LockIcon from '@material-ui/icons/Lock';
import {SlideContext} from "../../model/context";
import SlideshowIcon from '@material-ui/icons/Slideshow';
import {ifClass, useProperty} from "../../util/util";

const useStyles = makeStyles({
    root: {
        position: "fixed",
        top: 20,
        left: "50vw",
        transform: "translateX(-50%)",
        borderRadius: 5,
        padding: "0px 5px",
        // backgroundColor: "#cacaca",
        opacity: 0,
        zIndex: 100,
        "&:hover": {
            opacity: 1,
        }
    },
    lock: {
        opacity: 1,
    },
    button: {
        textAlign: "center",
        transition: "all 0.3s ease",
        color: "white",
        cursor: "pointer",
        fontSize: 30,
        "&:hover": {
            backgroundColor: "#aaaaaa",
        }
    }
});

type ToolbarProp = {}

const ToolbarView: React.FunctionComponent<ToolbarProp> = (props) => {
    const classes = useStyles();
    const context = useContext(SlideContext);
    const mode = useProperty(context.state.presentMode);
    const full = useProperty(context.state.fullScreen);
    const lock = useProperty(context.state.lockToolbar);
    return (
        <Paper className={classes.root + ifClass(lock, classes.lock)} elevation={3}>
            {mode === "present" ? (
                <Tooltip title={"Outline Mode"}>
                    <IconButton onClick={() => context.state.presentMode.value = "outline"}>
                        <ViewModuleIcon/>
                    </IconButton>
                </Tooltip>
            ) : (
                <Tooltip title={"Present Mode"}>
                    <IconButton onClick={() => context.state.presentMode.value = "present"}>
                        <SlideshowIcon/>
                    </IconButton>
                </Tooltip>)
            }
            {full ? (
                <Tooltip title={"Exit Full Screen"}>
                    <IconButton onClick={() => context.state.fullScreen.value = false}>
                        <FullscreenExitIcon/>
                    </IconButton>
                </Tooltip>
            ) : (
                <Tooltip title={"Full Screen"}>
                    <IconButton onClick={() => context.state.fullScreen.value = true}>
                        <FullscreenIcon/>
                    </IconButton>
                </Tooltip>
            )}
            {lock ? (
                <Tooltip title={"Unlock Toolbar"}>
                    <IconButton onClick={() => context.state.lockToolbar.value = false}>
                        <LockOpenIcon/>
                    </IconButton>
                </Tooltip>
            ) : (
                <Tooltip title={"Lock Toolbar"}>
                    <IconButton onClick={() => context.state.lockToolbar.value = true}>
                        <LockIcon/>
                    </IconButton>
                </Tooltip>
            )}
        </Paper>
    )
};

export default ToolbarView;