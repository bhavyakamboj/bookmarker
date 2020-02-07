import React from "react";
import { Button } from "primereact/button";
import {NavLink} from "react-router-dom";

const BookmarkOptions = ({ bookmark }) => {
  return (
    <div>
      {bookmark.tags.map(tag =>
          <NavLink key={tag} to={"/tags/"+tag}>
            <Button key={tag} label={tag} icon="pi pi-tags" className="m-1" />
          </NavLink>
          )}
    </div>
  );
};

export default BookmarkOptions;
