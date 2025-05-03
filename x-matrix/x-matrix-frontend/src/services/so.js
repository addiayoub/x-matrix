//fetch so 

import api from "./api";

const fetchSos = async (id) => {
    try {
      const response = await api.get(
        `/xmatrix/${id}/sos`
      );
      console.log(response.data);
    //   setSos(response.data);
    
    return response.data;
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }

  };

const addSo = async (data) => {
    try {
      const response = await api.post(
        `/sos?xMatrixId=${data.xMatrixId}`,
        {
          label: data.label,
          start: data.startDate,
          end: data.endDate,
        }
      );
  
      console.log("Added:", response.data);
    } catch (error) {
      console.error("Error adding objective:", error);
    }
  };

  const updateSo = async (data) => {
    try {
      const response = await api.put(
        `/sos/${data.soId}`,
        {
          label: data.label,
          start: data.startDate,
          end: data.endDate,
        }
      );
  
      console.log("Updated:", response.data);
    } catch (error) {
      console.error("Error updating objective:", error);
    }
  };

export { fetchSos , addSo , updateSo };