import React, { useState } from "react";
import { DataView, DataViewLayoutOptions } from "primereact/dataview";
import BookmarkGridItem from "./BookmarkGridItem";
import BookmarkListItem from "./BookmarkListItem";

const BookmarksList = ({ bookmarks }) => {
  const [layout, setLayout] = useState("list");

  const renderListItem = bookmark => {
    return (
      <div className="p-col-12" key={bookmark.id}>
        <BookmarkListItem bookmark={bookmark} />
      </div>
    );
  };

  const renderGridItem = bookmark => {
    return (
      <div className="p-col-4" key={bookmark.id}>
        <BookmarkGridItem bookmark={bookmark} />
      </div>
    );
  };

  const itemTemplate = (car, layout) => {
    if (!car) {
      return null;
    }
    if (layout === "list") return renderListItem(car);
    else if (layout === "grid") return renderGridItem(car);
  };

  const renderHeader = () => {
    return (
      <div className="p-grid">
        <div className="p-col-12" style={{ textAlign: "right" }}>
          <DataViewLayoutOptions
            layout={layout}
            onChange={e => setLayout(e.value)}
          />
        </div>
      </div>
    );
  };

  const header = renderHeader();

  return (
    <DataView
      value={bookmarks}
      layout={layout}
      header={header}
      itemTemplate={itemTemplate}
      paginatorPosition={"both"}
      paginator={true}
      rows={9}
    />
  );
};

export default BookmarksList;
