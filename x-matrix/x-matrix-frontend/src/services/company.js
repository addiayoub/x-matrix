import api from "./api";


const fetchCompanies = async () => {
    try {
      const response = await api.get(
        `/companies`
      );
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }

  }

const addCompany = async (data) => {
    try {
      const response = await api.post(
        `/companies`,
        {
         name : data.name,
         logo : data.logo,
         location : data.location,
        }
      );
      console.log("Added:", response.data);
      return response.data;
    } catch (error) {
      console.error("Error adding objective:", error);
    }
  }

  export { fetchCompanies , addCompany };