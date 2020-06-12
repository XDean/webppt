import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box} from "@material-ui/core";
import {XElement, XText} from "../../model/model";

const useStyles = makeStyles({});

type PlainProp = {
    text: XText
}

const PlainView: React.FunctionComponent<PlainProp> = (props) => {
    const paragraphs: string[][] = [];
    let paragraph: string[] = [];
    props.text.lines.forEach(line => {
        if (line.trim().length == 0) {
            if (paragraph.length != 0) {
                paragraphs.push(paragraph);
                paragraph = [];
            }
        } else {
            paragraph.push(line.split(" ").join("\u00A0"));
        }
    });
    if (paragraph.length != 0) {
        paragraphs.push(paragraph);
    }
    return (
        <Box>
            {paragraphs.map(p => (
                <p>
                    {p.map(line => <React.Fragment>
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