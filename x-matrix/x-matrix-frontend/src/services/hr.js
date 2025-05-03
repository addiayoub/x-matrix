import api from "./api";


const fetchHr = async () => {
    try {
      const response = await api.get(
         `/resources`
        
      );
      console.log('response', response.data);
    //   setSos(response.data);
    
    return response.data;
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }
  
  };


const addHr = async (data) => {
    try {
      const response = await api.post(
        `/resources`,
        {
          department: data.department,
        }
      );
  
      console.log("Added:", response.data);
    } catch (error) {
      console.error("Error adding objective:", error);
    }
  };

const updateHr = async (data) => {
    try {
      const response = await api.put(
        `/resources/${data.resourceId}`,
        {
          department: data.department,
        }
      );
  
      console.log("Updated:", response.data);
    } catch (error) {
      console.error("Error updating objective:", error);
    }
  };
export { fetchHr , addHr , updateHr };