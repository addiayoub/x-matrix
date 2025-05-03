import api from "./api";


const fetchIts = async (id) => {
    try {
      const response = await api.get(
        `/xmatrix/${id}/its`
      );
      console.log(response.data);
      
    //   setSos(response.data);
    return response.data;
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }

  }


const addIt = async (data) => {
    try {
      const response = await api.post(
        `/its?ipId=${data.ipId}`,
        {
          label: data.label,
          start: data.start,
          end: data.end,
          advancement: data.advancement,
          resourceId: data.resourceId,

        }
      );
  
      console.log("Added:", response.data);
    } catch (error) {
      console.error("Error adding objective:", error);
    }
  }

  const updateIt = async (data) => {
    try {
      const response = await api.put(
        `/its/${data.itId}`,
        {
          label: data.label,
          start: data.start,
          end: data.end,
          advancement: data.advancement,
          resourceId: data.resourceId,
        }
      );
  
      console.log("Updated:", response.data);
    } catch (error) {
      console.error("Error updating objective:", error);
    }
  }
export { fetchIts , addIt , updateIt };