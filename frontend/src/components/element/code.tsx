import React, {useContext, useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box, Chip} from "@material-ui/core";
import {XElement} from "../../model/model";
import {Controlled as CodeMirror} from 'react-codemirror2'
import "codemirror/addon/scroll/simplescrollbars.css"
import "codemirror/addon/scroll/simplescrollbars"
import "codemirror/lib/codemirror.css"
import {Editor} from "codemirror"
import {fetchText, SlideContext} from "../../model/context";
import {findLanguageByExt, findLanguageByName, Language} from "../../model/language";
import {getExtension} from "../../util/util";
import ReactDOM from 'react-dom';

const useStyles = makeStyles({
    wrapper: {
        position: "relative",
        width: "70%",
        marginTop: 10,
        marginBottom: 10,
        overflow: "hidden",

        border: "1px solid rgb(224, 224, 224)",
    },
    panel: {},
    topBar: {
        position: "absolute",
        top: 5,
        right: 10,
    },
    tag: {
        zIndex: 10,
        color: "#000",
        backgroundColor: "#fff",
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

    const [value, setValue] = useState("Loading...");
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
        require(`codemirror/mode/${lang.codemirrorJs}`);
    }

    return (
        <Box className={classes.wrapper}>
            <CodeMirror onBeforeChange={(editor, data, v) => setValue(v)} value={value}
                        editorDidMount={editor => {
                            const wrap = editor.getWrapperElement();
                            const panel = document.createElement("div");
                            panel.classList.add(classes.panel);
                            wrap.appendChild(panel);
                            ReactDOM.render((
                                <Box className={classes.topBar}>
                                    {lang &&
                                    <Chip className={classes.tag} label={lang.name} variant={"outlined"} size={"small"}
                                          clickable/>}
                                </Box>
                            ), panel);
                        }}
                        options={{
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