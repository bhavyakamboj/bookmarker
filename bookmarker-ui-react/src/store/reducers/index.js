import { combineReducers } from "redux";
import bookmarks from "./bookmarks";
import user from "./user";

export default combineReducers({
    bookmarks,
    user
});
