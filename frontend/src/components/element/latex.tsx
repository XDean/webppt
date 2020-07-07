import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {XElement} from "../../model/model";

const MathJax = require("react-mathjax").default;

const tex = `f(x) = \\int_{-\\infty}^\\infty
    \\hat f(\\xi)\\,e^{2 \\pi i \\xi x}
    \\,d\\xi`
console.log(MathJax)

const useStyles = makeStyles({});

type LatexProp = {
    element: XElement
}

const LatexView: React.FunctionComponent<LatexProp> = (props) => {
    const text = props.element.assertSingleTextLeaf();
    return (
        <MathJax.Provider>
            <MathJax.Node formula={text.lines.join("\n")}/>
        </MathJax.Provider>
    )
};

export default LatexView;