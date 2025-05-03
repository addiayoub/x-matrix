import { createSlice } from "@reduxjs/toolkit";
import { login } from "./AuthThunk";
import { jwtDecode } from "jwt-decode";

const initialState = {
  user: {},
  isAuthenticated: localStorage.getItem("token") ? true : false,
  role: null,
};

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    user: {},
    isAuthenticated: localStorage.getItem("token") ? true : false,
    role: null,
  },
  reducers: {
    setRole: (state, action) => {
      state.role = action.payload;
    },
    setUser: (state, action) => {
      state.user = action.payload;
    },
    setAuthenticated: (state, action) => {
      state.isAuthenticated = action.payload; 
    },
    logout: (state) => {
      localStorage.clear();
      state.isAuthenticated = false;
      state.role = null;
      state.user = {};
    },
  },
  extraReducers: (builder) => {
    builder.addCase(login.fulfilled, (state, action) => {
      const token = action.payload.data.token;
      if (token) {
        localStorage.setItem("token", token);
        const decoded = jwtDecode(token);
        state.isAuthenticated = true;
        state.user = decoded;
        state.role = decoded.role;
      }
    });

    builder.addCase(login.rejected, (state, action) => {
      localStorage.clear();
      state.isAuthenticated = false;
      state.role = null;
      state.user = {};
    });
  },
});

export const { setRole, setUser, setAuthenticated, logout } = authSlice.actions;
export default authSlice.reducer;
