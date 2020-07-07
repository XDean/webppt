import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {XText} from "../../model/model";
import ReactMarkdown from "react-markdown";
import "./md.scss"

const useStyles = makeStyles({});

type MarkdownProps = {
    text: XText
}

const MarkdownView: React.FunctionComponent<MarkdownProps> = (props) => {
    return (
        <ReactMarkdown className={"markdown-body"} source={props.text.lines.join("\n")} linkTarget={"_blank"}/>
    )
};

export default MarkdownView;