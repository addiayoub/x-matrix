import React, { useState } from "react";
import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";
import { useEffect } from "react";
import { jwtDecode } from "jwt-decode";
import { useDispatch } from "react-redux";
import { setAuthenticated, setRole, setUser } from "../features/auth/AuthSlice";

const Auth = ({ component: Component, requiredRoles = [], ...rest }) => {
  const dispatch = useDispatch();
  const isAuthenticated = useSelector((state) => state.auth?.isAuthenticated);
  const currentUser = useSelector((state) => state.auth?.user);
  const currentRole = currentUser?.role;

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (token) {
      try {
        const decoded = jwtDecode(token);
        dispatch(setUser(decoded)); 
        dispatch(setRole(decoded.role)); 
        dispatch(setAuthenticated(true)); 
      } catch (error) {
        console.error("Error decoding token:", error);
        dispatch(setAuthenticated(false)); 
      }
    } else {
      dispatch(setAuthenticated(false)); 
    }
  }, [dispatch]);  

  if (isAuthenticated === true && currentRole === undefined) {
    return <div>Loading...</div>;
  }

  if (!isAuthenticated) {
    console.log("User is not authenticated, redirecting to login.");
    return <Navigate to="/login" />;
  }

  if (
    requiredRoles.length > 0 &&
    !requiredRoles.some(
      (role) => role.toLowerCase() === (currentRole || "").toLowerCase()
    )
  ) {
    console.log('User role:', currentRole);
    console.log('Required roles:', requiredRoles);
    console.log("User does not have the required role.");
    return <Navigate to="/unauthorized" />;
  }

  console.log("Current user role:", currentRole);
  console.log("Required roles:", requiredRoles);

  return <Component {...rest} />;
};

export default Auth;
