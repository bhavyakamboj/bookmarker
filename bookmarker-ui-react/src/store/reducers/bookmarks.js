import * as actionTypes from "../actions/actionTypes";

const initialState = {
    allBookmarks: [],
    selectedTag: {},
    allTags: [],
    searchResults: []
};
const bookmarks = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.RECEIVE_BOOKMARKS:
            return {
                ...state,
                allBookmarks: action.payload || []
            };
        case actionTypes.RECEIVE_SELECTED_TAG:
            return {
                ...state,
                selectedTag: action.payload || []
            };
        case actionTypes.RECEIVE_ALL_TAGS:
            return {
                ...state,
                allTags: action.payload || []
            };
        case actionTypes.RECEIVE_SEARCH_BOOKMARKS:
            return {
                ...state,
                searchResults: action.payload || []
            };
        default:
            return state;
    }
};

export default bookmarks;
