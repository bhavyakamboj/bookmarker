import React, { useRef, useState, useEffect } from "react";
import {useDispatch, useSelector} from "react-redux";
import { fetchAllTags, createBookmark } from "../../store/actions/actionCreators";
import { Growl } from "primereact/growl";
import {InputText} from "primereact/inputtext";
import {AutoComplete} from 'primereact/autocomplete';

const AddBookmark = () => {
    const allTags = useSelector(state => state.bookmarks.allTags);
    let growl = useRef(null);
    const [url, setUrl] = useState("");
    const [title, setTitle] = useState("");
    const [tags, setTags] = useState([]);
    const [filteredTagsMultiple, setFilteredTagsMultiple] = useState(null);
    const dispatch = useDispatch();

    useEffect(() => {
        console.log("loading tags....");
        dispatch(fetchAllTags());
    }, []);

    const filterTagMultiple = (event) => {
        setTimeout(() => {
            let results = allTags.filter((tag) => {
                return tag.name.toLowerCase().startsWith(event.query.toLowerCase());
            });

            setFilteredTagsMultiple(results);
        }, 250);
    };

    const isFormValid = () => {
        return !(url === "");
    };

    const handleFormSubmit = e => {
        e.preventDefault();
        if (!isFormValid()) {
            growl.show({
                severity: "error",
                summary: "Error Message",
                detail: "Please enter all mandatory values"
            });
            return;
        }
        const newBookmark = {
            url: url,
            title: title,
            tags: tags.map(t => t.name)
        };
        console.log("NewBM", newBookmark);

        dispatch(
            createBookmark(newBookmark)
        );
        setUrl("");
        setTitle("");
        setTags([]);
    };

    return (
        <div className="container col-md-8">
            <Growl ref={el => (growl = el)} />
            <div className="card">
                <div className="card-header text-center">
                    <h3>Add New Bookmark</h3>
                </div>
                <div className="card-body">
                    <form onSubmit={e => handleFormSubmit(e)} className="row justify-content-center">
                        <div className="form-group col-md-10">
                            <label htmlFor="url">URL</label>
                            <InputText
                                id="url"
                                className="form-control col-md-12"
                                value={url}
                                onChange={e => setUrl(e.target.value)}
                            />
                        </div>
                        <div className="form-group col-md-10">
                            <label htmlFor="title">Title</label>
                            <InputText
                                id="title"
                                className="form-control col-md-12"
                                value={title}
                                onChange={e => setTitle(e.target.value)}
                            />
                        </div>

                        <div className="form-group col-md-10">

                            <AutoComplete value={tags}
                                          id="tags"
                                          inputClassName={"col-md-12"}
                                          suggestions={filteredTagsMultiple}
                                          completeMethod={filterTagMultiple}
                                          minLength={1}
                                          placeholder="Tags"
                                          field="name"
                                          multiple={true}
                                          onChange={(e) => setTags(e.value)} />

                        </div>

                        <div className="form-group col-md-10">
                            <button type="submit" className="btn btn-primary">
                                Add
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    );
};

export default AddBookmark;
