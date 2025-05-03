import api from "./api";

const fetchIps = async (id) => {
    try {
      const response = await api.get(
        `/xmatrix/${id}/ips`
      );
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }

  };

  const addIp = async (data) => {
    try {
      const response = await api.post(
        `/ips?aoId=${data.aoId}`,
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
  };

  const updateIp = async (data) => {
    try {
      const response = await api.put(
        `/ips/${data.ipId}`,
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
  }

export { fetchIps  , addIp , updateIp };