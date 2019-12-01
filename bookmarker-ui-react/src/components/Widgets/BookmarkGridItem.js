import React from "react";
import { Card } from "primereact/card";
import defImag from "../../space.jpg";
import BookmarkOptions from "./BoomarkOptions";

const styles = {
  headerImage: {
    height: "180px"
  },
  content: {
    width: "100%",
    height: "400px",
    padding: "1px"
  },
  footer: {}
};

const BookmarkGridItem = ({ bookmark }) => {
  const header = <img alt="Card" src={defImag} style={styles.headerImage} />;
  const footer = (
    <span style={styles.footer}>
      <BookmarkOptions bookmark={bookmark} />
    </span>
  );

  return (
    <div>
      <a
        rel="noopener noreferrer"
        target="_blank"
        href={bookmark.url}
        style={{ textDecoration: "none" }}
      >
        <Card
          title={bookmark.title}
          style={styles.content}
          className="ui-card-shadow"
          footer={footer}
          header={header}
        >
          <div>{bookmark.description}</div>
        </Card>
      </a>
    </div>
  );
};

export default BookmarkGridItem;
