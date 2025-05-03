import api from "./api";


const fetchMatrix = async (id) => {
        const response = await api.get(
        `/xmatrix/${id}`
        );
        console.log(response.data);
        return response.data;
    
    }

const addMatrix = async (data) => {
    try {
        const response = await api.post(
        `/xmatrix/`,
        data
        );
        console.log("Added:", response.data);
        return response
    } catch (error) {

        console.error("Error adding matrix:", error);
        return error.response;
    }
    }

const fetchMatrices = async () => {
    try {
        const response = await api.get("/xmatrix/");
        console.log('Fetched:', response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching matrices:", error);
    }
    }

const fetchMatrixHierarchy = async () => {
    try {
        const response = await api.get(
        `xmatrix/matrices-under-hierarchy`
        );
        console.log(response.data);
        return response.data;
    
    } catch (error) {
        console.error("Error fetching matrix hierarchy:", error);
    }
    }

const fetchMatricesUnderHierarchy = async () => {
    try {
        const response = await api.get(
        `xmatrix/all`
        );
        console.log(response.data);
        return response.data;
    
    } catch (error) {
        console.error("Error fetching matrix hierarchy:", error);
    }
    }
export { fetchMatrix , addMatrix , fetchMatrices , fetchMatrixHierarchy  , fetchMatricesUnderHierarchy };
