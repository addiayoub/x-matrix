import React from "react";

const DataTable = ({ data, columns, rowKey, cellKey, cellContent }) => (
  <table className="w-full h-full table-fixed border-collapse border border-orange-300">
    <tbody>
      {data &&
        data.map((row, rowIndex) => (
          <tr key={row[rowKey] || rowIndex}>
            {columns.map((col, colIndex) => (
              <td
                key={col[cellKey] || colIndex}
                className={`text-center align-middle max-w-full break-words h-fit border border-orange-700`}
                style={{ textAlign: "center", verticalAlign: "middle" }}
              >
                <div className="w-full h-full flex items-center justify-center">
                  {cellContent(row, col)}
                </div>
              </td>
            ))}
          </tr>
        ))}
    </tbody>
  </table>
);

export default DataTable;