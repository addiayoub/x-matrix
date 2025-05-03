import api from "./api";


const fetchAos = async (id) => {
    try {
      const response = await api.get(
        `/xmatrix/${id}/aos`
      );
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }

  }


const addAo = async (data) => {
    try {
      const response = await api.post(
        `/aos?soId=${data.soId}`,
        {
          label: data.label,
          start: data.start,
          end: data.end,
        
        }
      );
  
      console.log("Added:", response.data);
    } catch (error) {
      console.error("Error adding objective:", error);
    }
  }


  const updateAo = async (data) => {
    try {
      const response = await api.put(
        `/aos/${data.aoId}`,
        {
          label: data.label,
          start: data.start,
          end: data.end,
          
        }
      );
      console.log("Updated:", response.data);
    } catch (error) {
      console.error("Error updating objective:", error);
    }
  };

export { fetchAos , addAo , updateAo };