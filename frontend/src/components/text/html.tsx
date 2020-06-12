import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement, XText} from "../../model/model";
import ReactMarkdown from "react-markdown";

const useStyles = makeStyles({});

type HTMLProps = {
    text: XText
}

const HTMLView: React.FunctionComponent<HTMLProps> = (props) => {
    return (
        <div dangerouslySetInnerHTML={{__html: props.text.lines.join("\n")}}/>
    )
};

export default HTMLView;