import React from 'react';
import {Box} from "@material-ui/core";
import {XText} from "../../model/model";
import "./plain.scss"

type PlainProp = {
    text: XText
}

const PlainView: React.FunctionComponent<PlainProp> = (props) => {
    const paragraphs: string[][] = [];
    let paragraph: string[] = [];
    props.text.lines.forEach(line => {
        if (line.trim().length === 0) {
            if (paragraph.length !== 0) {
                paragraphs.push(paragraph);
                paragraph = [];
            }
        } else {
            paragraph.push(line.split(" ").join("\u00A0"));
        }
    });
    if (paragraph.length !== 0) {
        paragraphs.push(paragraph);
    }
    return (
        <Box className={"wp-text-plain"}>
            {paragraphs.map((p, index) => (
                <p key={index}>
                    {p.map((line, i) => <React.Fragment key={i}>
                            {line}
                            <br/>
                        </React.Fragment>
                    )}
                </p>
            ))}
        </Box>
    )
};

export default PlainView;