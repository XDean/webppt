import React, {useContext, useState} from 'react';
import {Box, IconButton, Paper, Slide, Tooltip} from "@material-ui/core";
import ViewModuleIcon from '@material-ui/icons/ViewModule';
import FullscreenIcon from '@material-ui/icons/Fullscreen';
import FullscreenExitIcon from '@material-ui/icons/FullscreenExit';
import LockOpenIcon from '@material-ui/icons/LockOpen';
import LockIcon from '@material-ui/icons/Lock';
import {SlideContext} from "../../model/context";
import SlideshowIcon from '@material-ui/icons/Slideshow';
import {useProperty} from "../../util/util";
import "./toolbar.scss"

type ToolbarProp = {};

const ToolbarView: React.FunctionComponent<ToolbarProp> = (props) => {
    const context = useContext(SlideContext);
    const mode = useProperty(context.state.presentMode);
    const full = useProperty(context.state.fullScreen);
    const lock = useProperty(context.state.lockToolbar);
    const [show, setShow] = useState(false);
    return (
        <Box className={"wp-toolbar"} onMouseEnter={() => setShow(true)} onMouseLeave={() => setShow(false)}>
            <Slide in={show || lock}>
                <Paper className={"content"} elevation={3}>
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
            </Slide>
        </Box>
    )
};

export default ToolbarView;