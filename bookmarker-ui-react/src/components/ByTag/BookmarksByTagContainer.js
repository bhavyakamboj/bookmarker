import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { fetchBookmarksByTag } from "../../store/actions/actionCreators";
import BookmarksList from "../Widgets/BookmarksList";

const BookmarksByTagContainer = (props) => {
    const selectedTag = useSelector(state => state.bookmarks.selectedTag) || {};
    const dispatch = useDispatch();

    useEffect(() => {
        console.log("loading bookmarks by tag:", props.tag);
        dispatch(fetchBookmarksByTag(props.tag));
    }, [props]);
    return <BookmarksList bookmarks={selectedTag.bookmarks} />;
};

export default BookmarksByTagContainer;
