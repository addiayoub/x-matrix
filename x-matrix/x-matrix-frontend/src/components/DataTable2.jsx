import React from 'react';

const DataTable2 = ({ data, columns, rowKey, cellKey, cellContent }) => (
<table className="w-full h-full table-fixed">
<tbody>
      {data && data.map((row, rowIndex) => (
        <tr
          key={row[rowKey] || rowIndex}
          className={`b ${rowIndex % 2 === 0 ? 'bg-gray-200' : 'bg-white'} border border-orange-700`}
        >
          {columns.map((col, colIndex) => (
            <td
              key={col[cellKey] || colIndex}
              className={` p-0.5 text-center align-middle max-w-full break-words w-fit h-fit text-xs `}
            >
              {cellContent(row, col)}
            </td>
          ))}
        </tr>
      ))}
    </tbody>
  </table>
);

export default DataTable2;