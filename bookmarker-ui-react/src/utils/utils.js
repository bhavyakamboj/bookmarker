import moment from "moment";

export const months = [
    { label: "Jan", value: 1 },
    { label: "Feb", value: 2 },
    { label: "Mar", value: 3 },
    { label: "Apr", value: 4 },
    { label: "May", value: 5 },
    { label: "Jun", value: 6 },
    { label: "Jul", value: 7 },
    { label: "Aug", value: 8 },
    { label: "Sep", value: 9 },
    { label: "Oct", value: 10 },
    { label: "Nov", value: 11 },
    { label: "Dec", value: 12 }
];

export const years = () => {
    const currentYear = moment().year();
    //console.log("CurrentYear", currentYear);
    return [currentYear - 3, currentYear - 2, currentYear - 1, currentYear, currentYear + 1, currentYear + 2].map(y => {
        return { label: y, value: y };
    });
};

export const formatDate = (date, format = "YYYY-MM-DD") => {
    return moment(date).format(format);
};
