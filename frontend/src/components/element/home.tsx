import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement} from "../../model/model";

const useStyles = makeStyles({});

type HomeProp = {
    element: XElement
}

const HomeView: React.FunctionComponent<HomeProp> = (props) => {
    const title = props.element.getParam("title")?.value;
    const subtitle = props.element.getParam("subtitle")?.value;
    const date = props.element.getParam("date")?.value;
    const author = props.element.getParam("author")?.value;
    return (
        <Box>
            <h1>{title || "@title"}</h1>
            {subtitle && <h2>{subtitle}</h2>}
            {date && <h2>{date}</h2>}
            {author && <Box>{author}</Box>}
        </Box>
    )
};

export default HomeView;