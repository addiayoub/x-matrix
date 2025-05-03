import api from "../../services/api";
import { createAsyncThunk } from "@reduxjs/toolkit";

const login = createAsyncThunk("login", async (data, { rejectWithValue }) => {
  try {
    const response = await api.post("auth/login", data);
    if (response.status >= 200 && response.status < 300) {
      return response;
    } else {
      return rejectWithValue(response);
    }
  } catch (error) {
    return rejectWithValue(error.response);
  }
});

export { login };