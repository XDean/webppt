import React, {useContext} from 'react';
import {Box} from "@material-ui/core";
import {SlideContext} from "../../model/context";
import {useProperty} from "../../util/util";
import "./page-number.scss"

type PageNumberProp = {}

const PageNumberView: React.FunctionComponent<PageNumberProp> = (props) => {
    const context = useContext(SlideContext);
    const current = useProperty(context.state.currentPage);
    return (
        <Box className={"wp-page-number"}>
            {current + 1} / {context.pages.length}
        </Box>
    )
};

export default PageNumberView;