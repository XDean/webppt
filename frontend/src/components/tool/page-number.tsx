import React, {useContext} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {SlideContext} from "../../model/context";
import {useProperty} from "../../util/util";

const useStyles = makeStyles({
    root: {
        color: "#8c8c8c",
        position: "absolute",
        bottom: 5,
        right: 20,
        pointerEvents: "none",
        zIndex: 100,
    }
});

type PageNumberProp = {}

const PageNumberView: React.FunctionComponent<PageNumberProp> = (props) => {
    const classes = useStyles();
    const context = useContext(SlideContext);
    const current = useProperty(context.state.currentPage);
    return (
        <Box className={classes.root}>
            {current + 1} / {context.pages.length}
        </Box>
    )
};

export default PageNumberView;