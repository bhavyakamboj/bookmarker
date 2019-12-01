import React, { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Redirect } from "react-router-dom";
import { login } from "../../store/actions/actionCreators";
import { InputText } from "primereact/inputtext";
import { Password } from "primereact/password";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const user = useSelector(state => state.user);
    const dispatch = useDispatch();

    const handleLogin = e => {
        e.preventDefault();
        if (!username.trim() || !password.trim()) {
            return;
        }
        dispatch(login({ username: username, password: password }));
    };

    if (user.access_token) {
        return <Redirect to="/" />;
    }
    return (
        <div className="container col-md-8">
            <div className="card">
                <div className="card-header text-center">
                    <h3>Login Form</h3>
                </div>
                <div className="card-body">
                    <form onSubmit={e => handleLogin(e)} className="row justify-content-center">
                        <div className="form-group col-md-10">
                            <label htmlFor="email">Email</label>
                            <InputText
                                id="email"
                                keyfilter="email"
                                className="form-control col-md-12"
                                value={username}
                                onChange={e => setUsername(e.target.value)}
                            />
                        </div>
                        <div className="form-group col-md-10">
                            <label htmlFor="password">Password</label>
                            <Password
                                id="password"
                                className="form-control col-md-12"
                                feedback={false}
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                            />
                        </div>
                        <div className="form-group col-md-10">
                            <button type="submit" className="btn btn-primary">
                                Login
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;
