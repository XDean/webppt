import React from 'react';
import {createStyles, makeStyles} from "@material-ui/core/styles";
import {Redirect, Route, Router, Switch} from "react-router-dom";
import {createBrowserHistory} from "history";
import SlideView from "./components/slide";

const useStyles = makeStyles(theme =>
    createStyles({
        root: {
            height: "100%",
            display: "grid",
            gridTemplateColumns: "100%",
            gridTemplateRows: "auto 1fr",
        },
    }),
);

const App: React.FunctionComponent = () => {

    const history = createBrowserHistory();
    const classes = useStyles();

    return (
        <Router history={history}>
            <Switch>
                <Route exact path={"/"}>
                    <div className="App">
                        Welcome to WebPPT
                    </div>
                </Route>
                <Route path={"/slide"}>
                    <SlideView/>
                </Route>
                <Route path={"*"}>
                    <Redirect to={"/"}/>
                </Route>
            </Switch>
        </Router>
    );
};

export default App;
