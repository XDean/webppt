import React, {useContext, useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement} from "../../model/model";
import {Controlled as CodeMirror} from 'react-codemirror2'
import "codemirror/addon/scroll/simplescrollbars.css"
import "codemirror/addon/scroll/simplescrollbars"
import "codemirror/lib/codemirror.css"
import {fetchText, SlideContext} from "../../model/context";
import {findLanguageByExt, findLanguageByName, Language} from "../../model/language";
import {getExtension} from "../../util/util";

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
    // boolean play;
    // CodeLanguage language;
    const context = useContext(SlideContext);
    const editable = props.element.getBoolParam("edit", true);
    // const resizable = props.element.getBoolParam("resize", false);
    const language = props.element.getStrParam("lang", "");
    const playable = props.element.getBoolParam("play", false);
    const theme = props.element.getStrParam("theme", "idea");
    const useURL = props.element.getStrParam("type", "url") === "url";

    const [value, setValue] = useState();
    const classes = useStyles();

    const textNode = props.element.assertSingleTextLeaf();
    useEffect(() => {
        if (useURL) {
            fetchText(context, textNode.lines[0])
                .then(t => setValue(t))
                .catch(e => setValue(e));
        } else {
            setValue(textNode.lines.join("\n"));
        }
    }, [props.element]);

    // theme
    try {
        require(`codemirror/theme/${theme}.css`);
    } catch (e) {
        // TODO
        console.error(e);
    }
    // language
    const lang = function () {
        if (language !== "") {
            return findLanguageByName(language);
        } else if (useURL) {
            return findLanguageByExt(getExtension(textNode.lines[0]))
        } else {
            return null;
        }
    }();
    if (lang) {
        try {
            require(`codemirror/mode/${lang.codemirrorJs}`);
        } catch (e) {
            // TODO
            console.error(e);
        }
    }

    return (
        <Box className={classes.wrapper}>
            <CodeMirror onBeforeChange={(editor, data, v) => setValue(v)} value={value} options={{
                lineNumbers: true,
                theme: theme,
                scrollbarStyle: "overlay",
                readOnly: !editable,
                mode: lang?.mime,
            }}/>
        </Box>
    )
};

export default CodeView;