import React from "react";

const DataTableRotation = ({ data, rowKey, cellKey, cellContent, style }) => (
<table className="w-full h-full  table-fixed">
<tbody>
      <tr>
        {data &&
          data.map((row, rowIndex) => (
            <td
              key={row[rowKey] || rowIndex}
              className={` p-3 text-center  align-middle max-w-full break-words border border-orange-700  h-fit text-xs ${rowIndex % 2 === 0 ? 'bg-gray-200' : 'bg-white'}`}
              style={style}
              cellKey={cellKey}
            >
              {cellContent(row)}
            </td>
          ))}
      </tr>
    </tbody>
  </table>
);

export default DataTableRotation;