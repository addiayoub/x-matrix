import React from "react";

const Trend = ({ data }) => {
  return <span className={`${data?.color} font-medium`}>{data?.key}</span>;
};

export default Trend;
