function getTailwindColor(percentage) {
  // percentage = percentage*100;
  if (percentage < 25) {
    return "text-red-500";
  } else if (percentage >= 25 && percentage < 50) {
    return "text-orange-500";
  } else if (percentage >= 50 && percentage < 75) {
    return "text-green-300";
  } else {
    return "text-green-700";
  }
}
const extractMatrixData = (matrix) => {
  if (!matrix) return { sos: [], aos: [], ips: [], its: [] };

  const allSOs = matrix.sos || [];
  const allAOs = [];
  const allIPs = [];
  const allITs = [];

  allSOs.forEach((so) => {
    if (so.aos) {
      allAOs.push(...so.aos);
      so.aos.forEach((ao) => {
        if (ao.ips) {
          allIPs.push(...ao.ips);
          ao.ips.forEach((ip) => {
            if (ip.its) {
              allITs.push(...ip.its);
            }
          });
        }
      });
    }
  });

  return {
    sos: allSOs,
    aos: allAOs,
    ips: allIPs,
    its: allITs,
  };
};

function formatToTwoDecimalPlaces(number) {
  // if (typeof number !== 'number') {
  //     throw new Error('Input must be a number');
  // }
  // return parseFloat(number.toFixed(2));
  if (typeof number !== "number") {
    throw new Error("Input must be a number");
  }
  return Math.floor(number);
}

function extractDateTime(isoString) {
  if (typeof isoString !== "string") {
    throw new Error("Input must be a string");
  }

  try {
    const date = new Date(isoString);
    const formattedDate = date.toISOString().split("T")[0]; // Extract date (YYYY-MM-DD)
    const time = date.toTimeString().split(" ")[0]; // Extract time (HH:mm:ss)
    return `${formattedDate} ${time}`;
  } catch (error) {
    console.error("Invalid ISO string:", isoString);
    throw new Error("Invalid ISO string");
  }
}

function getCharFromValue(value) {
  if (value == "BEHIND") {
    return {
      key: "B",
      color: "text-red-700",
    };
  }
  return {
    key: "A",
    color: "text-green-700",
  };
}

export {
  getTailwindColor,
  extractMatrixData,
  formatToTwoDecimalPlaces,
  extractDateTime,
  getCharFromValue
};
