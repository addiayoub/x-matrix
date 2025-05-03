import React, { useEffect } from "react";
import { IoMdAdd } from "react-icons/io";
import {
  Button,
  Checkbox,
  Label,
  Modal,
  Select,
  TextInput,
} from "flowbite-react";
import { useState } from "react";
import { Link } from "react-router-dom";
import api from "../../services/api";
import { addMatrix, fetchMatrices } from "../../services/matrix";
import { addUser, fetchRoles, fetchUsers } from "../../services/user";
import { fetchCompanies } from "../../services/company";
const Users = () => {
  const [openModal, setOpenModal] = useState(false);
  const [users, setUsers] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [roles, setRoles] = useState([]);
  const [selectedRoleName, setSelectedRoleName] = useState("");

  useEffect(() => {
    const loadUsers = async () => {
      const data = await fetchUsers();
      if (data) setUsers(data);
    };
    const loadCompanies = async () => {
      const data = await fetchCompanies();
      if (data) setCompanies(data);
    };
    const loadRoles = async () => {
      const data = await fetchRoles();
      if (data) setRoles(data);
    };
    loadUsers();
    loadCompanies();
    loadRoles();
  }, []);

  function onCloseModal() {
    setOpenModal(false);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData);
    console.log(data);

    try {
      const response = await addUser(data);
      console.log("Added:", response.data);
      setOpenModal(false);
      // Optionally, refresh the user list after adding a new user
      const updatedUsers = await fetchUsers();
      if (updatedUsers) setUsers(updatedUsers);
    } catch (error) {
      console.error("Error adding user:", error);
    }
  };

  const handleRoleChange = (e) => {
    const selectedId = e.target.value;
    const selectedRole = roles.find(
      (role) => role.id.toString() === selectedId,
    );
    setSelectedRoleName(selectedRole?.name || "");
  };
  return (
    <>
      <div className="flex flex-col gap-5">
        <button
          onClick={() => setOpenModal(true)}
          data-modal-target="crud-modal"
          data-modal-toggle="crud-modal"
          type="button"
          class=" mb-2 me-2 flex w-fit  flex-row  items-center gap-2 bg-[#3b5998] px-5  py-2.5  text-center text-sm font-medium text-white hover:bg-[#3b5998]/90"
        >
          <IoMdAdd />
          Add New User
        </button>

        {/* table */}
        {users && users.length > 0 ? (
          <div class="relative overflow-x-auto">
            <table class="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
              <thead class="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  <th scope="col" class="px-6 py-3">
                    ID
                  </th>
                  <th scope="col" class="px-6 py-3">
                    username
                  </th>{" "}
                  <th scope="col" class="px-6 py-3">
                    email
                  </th>
                  <th scope="col" class="px-6 py-3">
                    company
                  </th>
                  <th scope="col" class="px-6 py-3">
                    role ID
                  </th>
                  <th scope="col" class="px-6 py-3">
                    parent ID
                  </th>
                  {/* <th scope="col" class="px-6 py-3 text-right">
                    Actions
                  </th> */}
                </tr>
              </thead>
              <tbody>
                {users &&
                  users.map((user) => (
                    <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
                      <th
                        scope="row"
                        class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
                      >
                        {user.id}
                      </th>
                      <td class="px-6 py-4">{user.username}</td>{" "}
                      <td class="px-6 py-4">{user.email || ""}</td>
                      <td class="px-6 py-4">{user.company?.name || ""}</td>
                      <td class="px-6 py-4">{user.roleName || ""}</td>
                      <td class="px-6 py-4">{user.parentId || ""}</td>
                      {/* <td class="px-6 py-4 text-right">
                        <Link
                          class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                          to={`/matrices/show/${user.id}`}
                        >
                          View
                        </Link>
                      </td> */}
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="flex h-40 items-center justify-center">
            <p className="text-gray-500 dark:text-gray-400">
              No matrices found
            </p>
          </div>
        )}

        {/* modal */}

        <Modal show={openModal} size="md" onClose={onCloseModal} popup>
          <Modal.Header />
          <Modal.Body>
            <div className="space-y-6">
              <h3 className="text-xl font-medium text-gray-900 dark:text-white">
                Add New User
              </h3>
              <form
                action=""
                onSubmit={handleSubmit}
                className="flex flex-col gap-3"
              >
                <div>
                  <div className="mb-2 block">
                    <Label htmlFor="label" value="Username" />
                  </div>
                  <TextInput
                    id="label"
                    placeholder="username"
                    // value={label}
                    name="username"
                    // onChange={(event) => setlabel(event.target.value)}
                    required
                  />
                </div>
                <div>
                  <div className="mb-2 block">
                    <Label htmlFor="label" value="Email" />
                  </div>
                  <TextInput
                    id="label"
                    placeholder="email"
                    // value={label}
                    name="email"
                    // onChange={(event) => setlabel(event.target.value)}
                    required
                  />
                </div>
                <div>
                  <div className="mb-2 block">
                    <Label htmlFor="label" value="Password" />
                  </div>
                  <TextInput
                    id="label"
                    placeholder="password"
                    // value={label}
                    name="password"
                    // onChange={(event) => setlabel(event.target.value)}
                    required
                  />
                </div>

                <div>
                  <div className="mb-2 block">
                    <Label htmlFor="label" value="Role" />
                  </div>
                  <Select
                    id="label"
                    name="roleId"
                    onChange={handleRoleChange}
                    required
                  >
                    <option value="" disabled selected>
                      Select a role
                    </option>
                    {roles &&
                      roles.map((role) => (
                        <option key={role.id} value={role.id}>
                          {role.name}
                        </option>
                      ))}
                  </Select>
                </div>

                {(selectedRoleName === "MatrixOwner" ||
                  selectedRoleName == "DomainOwner"
                  || selectedRoleName == "GlobalOwner"
                ) && (
                  <div>
                    <div className="mb-2 block">
                      <Label htmlFor="label" value="Parent User ID" />
                    </div>
                    <Select id="label" name="parentId" required>
                      <option value="" disabled selected>
                        Select a parent user
                      </option>
                      {users &&
                        users.map((user) => (
                          <option key={user.id} value={user.id}>
                            {user.username}
                          </option>
                        ))}
                    </Select>
                  </div>
                )}

                {selectedRoleName != "SolutionOwner" && (
                  <div>
                    <div className="mb-2 block">
                      <Label htmlFor="label" value="Company ID" />
                    </div>
                    <Select id="label" name="companyId" required>
                      <option value="" disabled selected>
                        Select a company
                      </option>
                      {companies &&
                        companies.map((company) => (
                          <option key={company.id} value={company.id}>
                            {company.name}
                          </option>
                        ))}
                    </Select>
                  </div>
                )}
                <div className="w-full">
                  <button
                    type="submit"
                    class="w-full rounded-lg bg-primary-600 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-primary-700 focus:outline-none focus:ring-4 focus:ring-primary-300 dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
                  >
                    Add New User
                  </button>
                </div>
              </form>
            </div>
          </Modal.Body>
        </Modal>
      </div>
    </>
  );
};

export default Users;
