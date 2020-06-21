import React, {useContext} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {SlideContext} from "../../model/context";
import KeyboardArrowLeftIcon from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@material-ui/icons/KeyboardArrowRight';


const useStyles = makeStyles({
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
});

type NavigatorProp = {}

const NavigatorView: React.FunctionComponent<NavigatorProp> = (props) => {
    const classes = useStyles();
    const context = useContext(SlideContext);

    return (
        <Box>
            <Box className={classes.navigator + " " + classes.navigatorLeft} onClick={context.state.prevPage}>
                <KeyboardArrowLeftIcon/>
            </Box>
            <Box className={classes.navigator + " " + classes.navigatorRight} onClick={context.state.nextPage}>
                <KeyboardArrowRightIcon/>
            </Box>
        </Box>
    )
};

export default NavigatorView;