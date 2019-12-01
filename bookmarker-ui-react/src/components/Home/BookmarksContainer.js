import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { fetchAllBookmarks } from "../../store/actions/actionCreators";
import BookmarksList from "../Widgets/BookmarksList";

const BookmarksContainer = () => {
    const bookmarks = useSelector(state => state.bookmarks.allBookmarks);
    const dispatch = useDispatch();

    useEffect(() => {
        console.log("loading bookmarks....");
        dispatch(fetchAllBookmarks());
    }, []);

    return <BookmarksList bookmarks={bookmarks} />;
};

export default BookmarksContainer;
