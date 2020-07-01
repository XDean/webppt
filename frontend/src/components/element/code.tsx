import React, {useContext, useEffect, useState} from 'react';
import {Box, Button, Typography} from "@material-ui/core";
import {XElement} from "../../model/model";
import {Controlled as CodeMirror} from 'react-codemirror2'
import "codemirror/addon/scroll/simplescrollbars.css"
import "codemirror/addon/scroll/simplescrollbars"
import "codemirror/lib/codemirror.css"
import {SlideContext} from "../../model/context";
import {findLanguageByExt, findLanguageByName} from "../../model/language";
import {getExtension} from "../../util/util";
import {Resizable} from "re-resizable";
import {RunCodeEvent, RunLineEvent, RunStopEvent} from "../../model/ws-event";
import {TopicEvent} from "../../model/socket";
import {Scrollbars} from 'react-custom-scrollbars';
import "./code.scss"

type CodeProp = {
    element: XElement;
}

let globalRunCount = 0;

const CodeView: React.FunctionComponent<CodeProp> = (props) => {
    const context = useContext(SlideContext);
    const editable = props.element.getBoolParam("edit", true);
    // const resizable = props.element.getBoolParam("resize", false);
    const language = props.element.getStrParam("lang", "");
    const playable = props.element.getBoolParam("play", false);
    const theme = props.element.getStrParam("theme", "idea");
    const useURL = props.element.getStrParam("type", "url") === "url";

    const [value, setValue] = useState("Loading...");

    const textNode = props.element.assertSingleTextLeaf();
    useEffect(() => {
        if (useURL) {
            context.fetchText(textNode.lines[0])
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
    const [runId, setRunId] = useState(0);
    const [running, setRunning] = useState(false);
    const [outputs, setOutputs] = useState<RunLineEvent[]>([]);

    return (
        <Box className={"wp-code-wrapper"}>
            <CodeMirror onBeforeChange={(editor, data, v) => setValue(v)} value={value}
                        options={{
                            lineNumbers: true,
                            theme: theme,
                            scrollbarStyle: "overlay",
                            readOnly: !editable,
                            mode: lang?.mime,
                        }}/>
            <Box className={"wp-code-topbar"}>
                {lang &&
                <Button variant={"outlined"} size={"small"} style={{textTransform: "capitalize"}}
                >{lang.name}</Button>}
            </Box>
            {playable &&
            <Box className={"wp-code-bottom-bar"}>
                {!play && <Button variant={"outlined"} size={"small"} onClick={() => setPlay(true)}>Play</Button>}
                {play && (
                    <React.Fragment>
                        <Resizable defaultSize={{width: 300, height: 200}} className={"wp-code-output-wrapper"}
                                   minWidth={120} minHeight={60} style={{position: "absolute"}}>
                            <Scrollbars renderThumbVertical={({...props}) => <div
                                className={"scrollbar"} {...props}/>}>
                                <Box className={"wp-code-output-content"}>
                                    {outputs.map((line, index) => {
                                        switch (line.type) {
                                            case "STDOUT":
                                                return <Typography key={index}
                                                    className={"stdout"}>{line.message}</Typography>;
                                            case "STDERR":
                                                return <Typography key={index}
                                                    className={"stderr"}>{line.message}</Typography>;
                                            case "START":
                                                return <Typography key={index}
                                                    className={"start"}>{line.message}</Typography>;
                                            case "STOP":
                                                return <Typography key={index}
                                                    className={"stop"}>stopped</Typography>;
                                            case "DONE":
                                                return <Typography key={index}
                                                    className={"done"}>exit code: {line.message}</Typography>;
                                            case "ERROR":
                                                return <Typography key={index}
                                                    className={"error"}>{line.message}</Typography>;
                                        }
                                    })}
                                </Box>
                            </Scrollbars>
                        </Resizable>
                        <Button variant={"outlined"} size={"small"} disabled={running} onClick={() => {
                            const theId = ++globalRunCount;
                            setRunId(theId);
                            setRunning(true);
                            setOutputs([]);
                            context.ws.addHandler({
                                topics: ["code"],
                                handle(event: TopicEvent<any>): boolean {
                                    switch (event.event) {
                                        case "line":
                                            const e = event.payload as RunLineEvent;
                                            if (e.id === theId) {
                                                setOutputs(o => [...o, e]);
                                            }
                                            break;
                                        case "close":
                                            setRunning(false);
                                            return true;
                                    }
                                    return false;
                                }
                            });
                            context.ws.send(new TopicEvent<RunCodeEvent>("code", "run", {
                                id: theId,
                                content: value,
                                language: lang!.name,
                            }))
                        }}>Run</Button>
                        <Button variant={"outlined"} size={"small"} disabled={!running} onClick={() => {
                            context.ws.send(new TopicEvent<RunStopEvent>("code", "stop", {id:runId}))
                        }}>Stop</Button>
                        <Button variant={"outlined"} size={"small"} onClick={() => setPlay(false)}>Close</Button>
                    </React.Fragment>
                )}
            </Box>}
        </Box>
    )
};

export default CodeView;