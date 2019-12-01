import React from "react";
import { Button } from "primereact/button";

const BookmarkOptions = ({ bookmark }) => {
  return (
    <div>
      <Button icon="pi pi-share-alt" className="m-1" />
      <Button icon="pi pi-heart" className="m-1" />
      <Button icon="pi pi-tags" className="m-1" />
      <Button icon="pi pi-inbox" className="m-1" />
      <Button icon="pi pi-trash" className="m-1" />
    </div>
  );
};

export default BookmarkOptions;
