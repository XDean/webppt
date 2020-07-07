import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {XText} from "../../model/model";
import ReactMarkdown from "react-markdown";
import "./md.scss"

const MathJax = require("react-mathjax").default;
const RemarkMathPlugin = require('remark-math');

console.log(RemarkMathPlugin)

const useStyles = makeStyles({});

type MarkdownProps = {
    text: XText
}

const MarkdownView: React.FunctionComponent<MarkdownProps> = (props) => {
    return (
        <MathJax.Provider>
            <ReactMarkdown className={"markdown-body"} source={props.text.lines.join("\n")} linkTarget={"_blank"}
                           plugins={[RemarkMathPlugin]}
                           renderers={{
                               math: (props) =>
                                   <MathJax.Node formula={props.value}/>,
                               inlineMath: (props) =>
                                   <MathJax.Node inline formula={props.value}/>
                           }}/>
        </MathJax.Provider>
    )
};

export default MarkdownView;