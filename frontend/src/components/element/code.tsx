import React, {useContext, useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box, Button, Chip} from "@material-ui/core";
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
import {Resizable} from "re-resizable";

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
        top: 2,
        right: 5,
    },
    bottomBar: {
        position: "absolute",
        bottom: 4,
        right: 5,
    },
    toolButton: {
        zIndex: 10,
        color: "#000",
        backgroundColor: "#fff",
        textTransform: "none",
        minWidth: 0,
        padding: "2px 4px",
        margin: "0 4px",
        "&:hover": {
            backgroundColor: "#fff",
        }
    },
    playOutput: {
        backgroundColor: "white",
        zIndex: 10,
        position: "absolute",
        right: 5,
        bottom: 30,
    }
});

type CodeProp = {
    element: XElement;
}

const CodeView: React.FunctionComponent<CodeProp> = (props) => {
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

    const [play, setPlay] = useState(false);

    return (
        <Box className={classes.wrapper}>
            <CodeMirror onBeforeChange={(editor, data, v) => setValue(v)} value={value}
                        options={{
                            lineNumbers: true,
                            theme: theme,
                            scrollbarStyle: "overlay",
                            readOnly: !editable,
                            mode: lang?.mime,
                        }}/>
            <Box className={classes.topBar}>
                {lang &&
                <Button className={classes.toolButton} variant={"outlined"} size={"small"}
                        style={{textTransform: "capitalize"}}
                >{lang.name}</Button>}
            </Box>
            <Box className={classes.bottomBar}>
                {!play && <Button variant={"outlined"} size={"small"} onClick={() => setPlay(true)}
                                  className={classes.toolButton}>Play</Button>}
                {play && (
                    <React.Fragment>
                        <Resizable defaultSize={{width: 300, height: 200}} className={classes.playOutput}
                                   minWidth={120} minHeight={60} style={{position:"absolute"}}
                        >
                            <Box>

                            </Box>
                        </Resizable>
                        <Button variant={"outlined"} size={"small"}
                                className={classes.toolButton}>Run</Button>
                        <Button variant={"outlined"} size={"small"}
                                className={classes.toolButton}>Stop</Button>
                        <Button variant={"outlined"} size={"small"} onClick={() => setPlay(false)}
                                className={classes.toolButton}>Close</Button>
                    </React.Fragment>
                )}
            </Box>
        </Box>
    )
};

export default CodeView;