import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "./Home/Home";
import NavBar from "./Layout/NavBar";
import Login from "./Login/Login";

const App = () => (
    <div className="App">
        <NavBar />
        <main role="main" className="container">
            <Switch>
                <Route path="/login" component={Login} />
                <Route component={Home} />
            </Switch>
        </main>
    </div>
);

export default App;
