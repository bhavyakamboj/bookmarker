import axios from "./axios";
import * as actionTypes from "./actionTypes";

export function login(credentials) {
    return dispatch => {
        return axios.post("/api/auth/login", credentials)
            .then(response => {
                console.log("auth success: ", response.data);
                dispatch({
                    type: actionTypes.LOGIN_SUCCESS,
                    payload: response.data
                });
                localStorage.setItem("access_token", response.data.access_token);
                //window.location = "/";
            })
            .catch(e => {
                console.log("login error", e);
                dispatch({
                    type: actionTypes.LOGIN_FAILURE
                });
            });
    };
}

export function fetchAllBookmarks() {
    return dispatch => {
        return axios("/api/bookmarks")
            .then(response => {
                return dispatch({
                    type: actionTypes.RECEIVE_BOOKMARKS,
                    payload: response.data.data
                });
            })
            .catch(e => console.log("error", e));
    };
}

export function searchBookmarks(query) {
    return dispatch => {
        return axios("/api/bookmarks/search?query=" + query)
            .then(response => {
                return dispatch({
                    type: actionTypes.RECEIVE_SEARCH_BOOKMARKS,
                    payload: response.data
                });
            })
            .catch(e => console.log("error", e));
    };
}

export function createBookmark(bookmark) {
    
    return dispatch => {
        return axios
            .post("/api/bookmarks", bookmark)
            .then(response => {
                dispatch(fetchAllBookmarks());
                return dispatch({
                    type: actionTypes.CREATE_BOOKMARK_SUCCESS,
                    payload: response.data
                });
            })
            .catch(e => console.log("error", e));
    };
}

