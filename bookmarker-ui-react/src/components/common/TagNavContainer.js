import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { fetchAllTags } from "../../store/actions/actionCreators";
import TagNav from "../Widgets/TagNav";

const TagNavContainer = () => {
    const tags = useSelector(state => state.bookmarks.allTags);
    const dispatch = useDispatch();

    useEffect(() => {
        console.log("loading tags....");
        dispatch(fetchAllTags());
    }, []);

    return <TagNav tags={tags} />;
};

export default TagNavContainer;
