import React, {useContext} from 'react';
import {Box} from "@material-ui/core";
import {SlideContext} from "../../model/context";
import KeyboardArrowLeftIcon from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@material-ui/icons/KeyboardArrowRight';
import "./navigator.scss"

type NavigatorProp = {}

const NavigatorView: React.FunctionComponent<NavigatorProp> = (props) => {
    const context = useContext(SlideContext);
    return (
        <Box>
            <Box className={"wp-navigator left"} onClick={context.prevPage}>
                <KeyboardArrowLeftIcon/>
            </Box>
            <Box className={"wp-navigator right"} onClick={context.nextPage}>
                <KeyboardArrowRightIcon/>
            </Box>
        </Box>
    )
};

export default NavigatorView;