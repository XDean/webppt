import React, {useContext} from 'react';
import {XElement} from "../../model/model";
import {SlideContext} from "../../model/context";

type ImageProp = {
    element: XElement
}

const ImageView: React.FunctionComponent<ImageProp> = (props) => {
    const ctx = useContext(SlideContext);
    const text = props.element.assertSingleTextLeaf();

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