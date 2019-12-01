import React from "react";
import { render } from "@testing-library/react";
import BookmarksList from "./BookmarksList";

test("render bookmarks when <BookmarksList/> component is mounted", async () => {
    const bookmarks = [
        { id: 1, title: "bookmark 1", description: "bookmark 1" },
        { id: 2, title: "bookmark 2", description: "bookmark 2" },
    ];
    const { container } = render(<BookmarksList bookmarks={bookmarks} />);
    expect(container.querySelectorAll(".p-datatable-row").length).toBe(2);
});
