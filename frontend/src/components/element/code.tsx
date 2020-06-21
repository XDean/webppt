import React, {useContext, useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement} from "../../model/model";
import {Controlled as CodeMirror} from 'react-codemirror2'
import "codemirror/addon/scroll/simplescrollbars.css"
import "codemirror/addon/scroll/simplescrollbars"
import "codemirror/lib/codemirror.css"
import "codemirror/theme/idea.css"
import {fetchText, resolveURL, SlideContext} from "../../model/context";
import {Resizable} from 're-resizable';

const useStyles = makeStyles({
    wrapper: {
        width: "70%",
        marginTop: 10,
        marginBottom: 10,
        overflow: "hidden",

        border: "1px solid rgb(224, 224, 224)",
    }
});

type CodeProp = {
    element: XElement;
}

const CodeView: React.FunctionComponent<CodeProp> = (props) => {
    // int id;
    // boolean readonly;
    // boolean resize;
    // boolean play;
    // CodeType type;
    // CodeLanguage language;
    // String content;
    // String theme;
    // CommonElementModel common;
    const context = useContext(SlideContext);
    const editable = props.element.getBoolParam("edit", true);
    const resizable = props.element.getBoolParam("resize", false);
    const playable = props.element.getBoolParam("play", false);
    const theme = props.element.getStrParam("theme", "idea");
    const useURL = props.element.getStrParam("type", "url") === "url";

    const [value, setValue] = useState();
    const classes = useStyles();

    useEffect(() => {
        const textNode = props.element.assertSingleTextLeaf();
        if (useURL) {
            fetchText(context, textNode.lines[0])
                .then(t => setValue(t))
                .catch(e => setValue(e));
        } else {
            setValue(textNode.lines.join("\n"));
        }
    }, [props.element]);

    const box = <CodeMirror onBeforeChange={(editor, data, v) => setValue(v)} value={value} options={{
        lineNumbers: true,
        theme: theme,
        scrollbarStyle: "overlay",
        readOnly: !editable,

    }}/>;
    if (resizable) {
        return (
            <Resizable
                defaultSize={{
                    width:320,
                    height:200,
                }}
            >
                {box}
            </Resizable>
        )
    }
    return (
        <Box className={classes.wrapper}>
            {box}
        </Box>
    )
};

export default CodeView;