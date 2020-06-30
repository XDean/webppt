import React, {useContext} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {XElement} from "../../model/model";
import {SlideContext} from "../../model/context";

const useStyles = makeStyles({});

type ImageProp = {
    element: XElement
}

const ImageView: React.FunctionComponent<ImageProp> = (props) => {
    const ctx = useContext(SlideContext);
    const text = props.element.assertSingleTextLeaf();

    // const url = new URL(props.el)
    return (
        <React.Fragment>
            {text.lines.map((path, i) => {
                return (
                    <div key={i}>
                        <img src={ctx.resolveResourceURL(path).href}/>
                    </div>
                );
            })}
        </React.Fragment>
    )
};

export default ImageView;