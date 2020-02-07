import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "./Home/Home";
import BookmarksByTag from "./ByTag/BookmarksByTag";
import NavBar from "./Layout/NavBar";
import Login from "./Login/Login";
import AddBookmark from "./AddBookmark/AddBookmark";

const App = () => (
    <div className="App">
        <NavBar />
        <main role="main" className="container">
            <Switch>
                <Route path="/login" component={Login} />
                <Route path="/add-bookmark" component={AddBookmark} />
                <Route path="/tags/:tag" component={BookmarksByTag} />
                <Route component={Home} />
            </Switch>
        </main>
    </div>
);

export default App;