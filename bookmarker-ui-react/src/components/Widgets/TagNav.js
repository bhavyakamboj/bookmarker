import React from "react";
import { Button } from "primereact/button";
import {NavLink} from "react-router-dom";

const TagNav = (props) => {
    const tags = props.tags || [];
    return (
        <div>
            {
                tags.map(tag =>
                    <NavLink key={tag.id} to={"/tags/"+tag.name}>
                        <Button label={tag.name} icon="pi pi-tags" className="m-1" />
                    </NavLink>
                )
            }
        </div>
    );
};

export default TagNav;
