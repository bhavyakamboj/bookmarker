import * as actionTypes from "../actions/actionTypes";

const user = (state = {}, action) => {
    switch (action.type) {
        case actionTypes.LOGIN_SUCCESS:
            return action.payload || {};

        case actionTypes.LOGOUT:
            return {};
        default:
            return state;
    }
};

export default user;
