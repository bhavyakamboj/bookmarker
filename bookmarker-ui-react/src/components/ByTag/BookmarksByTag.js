import React from "react";
import { useParams } from "react-router-dom";
import BookmarksByTagContainer from "./BookmarksByTagContainer";
import TagNavContainer from "../common/TagNavContainer";

const BookmarksByTag = () => {
    let { tag } = useParams();
    console.log('selected tag:', tag);
    return (
        <div className="container">
            <div className="row">
                <div className="col-12">
                    <div className="mt-3">
                        <div className="row">
                            <div className="col-9">
                                <BookmarksByTagContainer tag={tag}/>
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

export default BookmarksByTag;
