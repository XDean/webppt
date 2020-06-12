import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement, XText} from "../../model/model";
import ReactMarkdown from "react-markdown";

const useStyles = makeStyles({});

type MarkdownProps = {
    text: XText
}

const MarkdownView: React.FunctionComponent<MarkdownProps> = (props) => {
    return (
        <ReactMarkdown source={props.text.lines.join("\n")} linkTarget={"_blank"}/>
    )
};

export default MarkdownView;