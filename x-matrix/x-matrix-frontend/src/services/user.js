import api from "./api";

const fetchUsers = async () => {
  try {
    const response = await api.get(`/users/`);
    console.log(response.data);
    return response.data;
  } catch (error) {
    console.error("Error fetching users:", error);
  }
};

const addUser = async (data) => {
  try {
    const response = await api.post(`/auth/register`, {
      username: data.username,
      email: data.email,
      password: data.password,
      roleId: data.roleId,
      parentId: data.parentId,
      companyId: data.companyId,
    });
    console.log("Added:", response.data);
    return response.data;
  } catch (error) {
    console.error("Error adding user:", error);
  }
};

const fetchRoles = async () => {
  try {
    const response = await api.get(`/roles`);
    console.log(response.data);
    return response.data;
  } catch (error) {
    console.error("Error fetching roles:", error);
  }
}

const filterRolesByRole = (roles, role) => {
  console.log("Filtering roles:", roles, "for role:", role);
  return roles.filter((r) => r.name == role);
};
export { fetchUsers, addUser  , fetchRoles };
