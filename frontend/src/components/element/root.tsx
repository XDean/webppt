import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement} from "../../model/model";

const useStyles = makeStyles({});

type RootProp = {
    element: XElement
}

const RootView: React.FunctionComponent<RootProp> = (props) => {
    console.log(props.element);
    return (
        <Box>
            {props.element.name}
        </Box>
    )
};

export default RootView;