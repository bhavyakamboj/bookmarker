import React from "react";
import { useSelector } from "react-redux";
import { NavLink } from "react-router-dom";
import { cleanState } from "../../store/localStorage";

const NavBar = () => {
    const user = useSelector(state => state.user);

    const logoutHandler = () => {
        cleanState();
        window.location = "/login";
    };

    let authenticatedLinks;

    if (user.access_token) {
        authenticatedLinks = (
            <ul className="navbar-nav">
                <li className="nav-item">
                    <button className="btn btn-outline-success my-2 my-sm-0" onClick={logoutHandler}>
                        Logout
                    </button>
                </li>
            </ul>
        );
    } else {
        authenticatedLinks = (
            <ul className="navbar-nav">
                <li className="nav-item">
                    <NavLink className="nav-link" to="/login">
                        Login
                    </NavLink>
                </li>
            </ul>
        );
    }
    return (
        <nav className="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
            <NavLink className="navbar-brand" to="/">
                Bookmarker
            </NavLink>
            <button
                className="navbar-toggler"
                type="button"
                data-toggle="collapse"
                data-target="#navbarCollapse"
                aria-controls="navbarCollapse"
                aria-expanded="false"
                aria-label="Toggle navigation"
            >
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarCollapse">
                <ul className="navbar-nav mr-auto">
                    {/*
                    <li className="nav-item">
                        <NavLink className="nav-link" to="/">
                            Home <span className="sr-only">(current)</span>
                        </NavLink>
                    </li>*/}
                </ul>
                {authenticatedLinks}
            </div>

        </nav>
    );
};

export default NavBar;
