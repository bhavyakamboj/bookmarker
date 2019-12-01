import React, { useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { createBookmark } from "../../store/actions/actionCreators";
import { Growl } from "primereact/growl";

const AddBookmark = () => {
  let growl = useRef(null);
  const [url, setUrl] = useState("");
  const [description, setDescription] = useState("");

  const dispatch = useDispatch();

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
    dispatch(
      createBookmark({
        url: url
      })
    );
    setUrl("");
  };

  return (
    <div className="">
      <Growl ref={el => (growl = el)} />
      <form onSubmit={e => handleFormSubmit(e)} className="form-inline">
        <input
          id="url"
          className="form-control form-control-lg col-md-10"
          placeholder="Enter URL"
          value={url}
          onChange={e => setUrl(e.target.value)}
        />
        <button type="submit" className="btn btn-primary btn-lg ml-1">
          Add
        </button>
      </form>
    </div>
  );
};

export default AddBookmark;
