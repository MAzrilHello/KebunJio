import moment from "moment";

const sanitizeDateTime = (dateTime) => {
    return dateTime ? moment(dateTime).format("YYYY-MM-DDTHH:mm:ss") : null;
  };

  export default sanitizeDateTime;