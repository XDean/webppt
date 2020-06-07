import React from 'react';
import {createStyles, makeStyles} from '@material-ui/core/styles';
import {useQuery} from "../util/util";

const useStyles = makeStyles(theme => createStyles({}));

type SlideProp = {}

const SlideView: React.FunctionComponent<SlideProp> = (props) => {
    const query = useQuery();

    return (
        <div>
            Slide {query.get("path")}
        </div>
    )
};

export default SlideView;