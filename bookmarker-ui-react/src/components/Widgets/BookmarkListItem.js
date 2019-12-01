import React from "react";
import { Card } from "primereact/card";
import BookmarkOptions from "./BoomarkOptions";

const BookmarkListItem = ({ bookmark }) => {
  return (
    <React.Fragment>
      <Card className="p-2">
        <div>
          <h5>
            <a
              rel="noopener noreferrer"
              target="_blank"
              href={bookmark.url}
              style={{ textDecoration: "none" }}
            >
              {bookmark.title}
            </a>
          </h5>
        </div>
        <div>
          <BookmarkOptions bookmark={bookmark} />
        </div>
      </Card>
    </React.Fragment>
  );
};

export default BookmarkListItem;
