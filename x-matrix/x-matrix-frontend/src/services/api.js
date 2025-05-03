import axios from "axios";

const api = axios.create({
  // baseURL: "http://4.233.222.229:9090/api/",
  
  baseURL: "http://localhost:9090/api/",
  // baseURL: "http://localhost:8080/api/",
  
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
    "token" : 'Bearer ' + localStorage.getItem('token')
    
  },
});

api.interceptors.request.use(
  (config) => {
    if (config.url !== "/login") {
      const token = localStorage.getItem("token");
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response.status === 401) {
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;