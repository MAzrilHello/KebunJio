import moment from "moment";

const sanitizeDateTime = (dateTime) => {
    return dateTime ? moment(dateTime).format("MMMM D, YYYY [at] h:mm A") : null;
  };

  export default sanitizeDateTime;