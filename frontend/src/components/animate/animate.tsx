import React, {useContext, useEffect, useRef, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Box, Button} from "@material-ui/core";
import {XElement} from "../../model/model";
import {CSSTransition, Transition} from 'react-transition-group';
import "./animate.scss"
import {SlideContext, SlideContextData} from "../../model/context";
import {useProperty} from "../../util/util";

const useStyles = makeStyles({});

type AnimateProp = {
    element: XElement
}

const AnimateView: React.FunctionComponent<AnimateProp> = (props) => {
    const context = useContext(SlideContext);
    const page = context.getPage(props.element)!;
    const [index, setIndex] = useState(-1);
    const current = useProperty(page.currentAnimate);
    const isIn = current > index;
    useEffect(() => {
        setIndex(page.animates.value.length);
        page.animates.update(es => {
            es.push(props.element);
        });
    }, [context, props.element, page, setIndex]);
    return (
        <Box>
            <CSSTransition in={isIn} timeout={1000} classNames="test" mountOnEnter unmountOnExit>
                {props.children}
            </CSSTransition>
        </Box>
    )
};

export default AnimateView;