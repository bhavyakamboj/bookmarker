import React from "react";
import AddBookmark from "./AddBookmark";
import BookmarksContainer from "./BookmarksContainer";

const Home = () => {
    return (
        <>
            <div className="container">
                <div className="row">
                    <div className="col-12">
                        <div>
                            <AddBookmark />
                        </div>
                        <div className="mt-3">
                            <BookmarksContainer />
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default Home;
