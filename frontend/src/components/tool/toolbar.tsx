import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import NavigatorView from "./navigator";

const useStyles = makeStyles({
    root: {

    }
});

type ToolbarProp = {}

const ToolbarView: React.FunctionComponent<ToolbarProp> = (props) => {
    const classes = useStyles();
    return (
        <Box className={classes.root}>
            <a id="page-outline-button" title="Outline"><i className="fa fa-th-large fa-fw"></i></a>
            <a id="page-fullscreen-button" title="Fullscreen"><i className="fa fa-arrows-alt fa-fw"></i></a>
            <a id="page-lock-button" title="Lock Toolbar"><i className="fa fa-lock fa-fw"></i></a>
        </Box>
    )
};

export default ToolbarView;