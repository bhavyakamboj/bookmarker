import React from "react";
import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import store from "./store/configureStore";
import { BrowserRouter as Router } from "react-router-dom";
import App from "./components/App";
import "@fortawesome/fontawesome-free/css/all.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "primereact/resources/themes/nova-light/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";
import "./index.css";

ReactDOM.render(
  <Provider store={store}>
    <Router>
      <App />
    </Router>
  </Provider>,
  document.getElementById("root")
);
