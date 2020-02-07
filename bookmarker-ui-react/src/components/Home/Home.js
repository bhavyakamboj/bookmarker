import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { fetchAllBookmarks } from "../../store/actions/actionCreators";
import BookmarksList from "../Widgets/BookmarksList";
import AddBookmark from "../common/AddBookmark";
import TagNavContainer from "../common/TagNavContainer";

const Home = () => {
    const bookmarks = useSelector(state => state.bookmarks.allBookmarks);
    const dispatch = useDispatch();

    useEffect(() => {
        console.log("loading bookmarks....");
        dispatch(fetchAllBookmarks());
    }, []);

    return (
        <div className="container">
            <div className="row">
                <div className="col-12">
                    <div>
                        <AddBookmark />
                    </div>
                    <div className="mt-3">
                        <div className="row">
                            <div className="col-9">
                                <BookmarksList bookmarks={bookmarks} />
                            </div>
                            <div className="col-3">
                                <TagNavContainer />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;
