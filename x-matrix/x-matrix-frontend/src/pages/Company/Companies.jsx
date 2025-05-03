import React, { useEffect } from "react";
import { IoMdAdd } from "react-icons/io";
import { Button, Checkbox, Label, Modal, TextInput } from "flowbite-react";
import { useState } from "react";
import { Link } from "react-router-dom";
import api from "../../services/api";
import { addCompany, fetchCompanies } from "../../services/company";
const Companies = () => {
  const [openModal, setOpenModal] = useState(false);
  const [Companies, setCompanies] = useState([]);

  useEffect(() => {
    const loadCompanies = async () => {
      const data = await fetchCompanies();
      if (data) setCompanies(data);
    };
    loadCompanies();
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
      const response = await addCompany(data);

      const updatedCompanies = await fetchCompanies();
      setCompanies(updatedCompanies);
      setOpenModal(false);
      alert("company created successfully!");
    } catch (error) {
      alert("Error creating company:", error.message);
    }
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
          Add New Company
        </button>

        {/* table */}
        {Companies && Companies.length > 0 ? (
          <div class="relative overflow-x-auto">
            <table class="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
              <thead class="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  <th scope="col" class="px-6 py-3">
                    ID
                  </th>
                  <th scope="col" class="px-6 py-3">
                    Name
                  </th>{" "}
                  <th scope="col" class="px-6 py-3">
                    Location{" "}
                  </th>
                  <th scope="col" class="px-6 py-3">
                    Logo{" "}
                  </th>
                  {/* <th scope="col" class="px-6 py-3 text-right">
                  Actions
                </th> */}
                </tr>
              </thead>
              <tbody>
                {Companies &&
                  Companies.map((company) => (
                    <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
                      <th
                        scope="row"
                        class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
                      >
                        {company.id}
                      </th>
                      <td class="px-6 py-4">{company.name}</td>{" "}
                      <td class="px-6 py-4">{company.location || ""}</td>
                      <td class="px-6 py-4">{company.logo || ""}</td>
                      {/* <td class="px-6 py-4 text-right">
                      <Link
                        class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                        to={`/Companies/show/${company.id}`}
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
              No Companies found
            </p>
          </div>
        )}

        {/* modal */}

        <Modal show={openModal} size="md" onClose={onCloseModal} popup>
          <Modal.Header />
          <Modal.Body>
            <div className="space-y-6">
              <h3 className="text-xl font-medium text-gray-900 dark:text-white">
                Generate New company
              </h3>
              <form
                action=""
                onSubmit={handleSubmit}
                className="flex flex-col gap-3"
              >
                <div>
                  <div className="mb-2 block">
                    <Label htmlFor="label" value="Name" />
                  </div>
                  <TextInput
                    id="name"
                    placeholder="Name"
                    // value={name}
                    name="name"
                    // onChange={(event) => setlabel(event.target.value)}
                    required
                  />
                </div>
                <div>
                  <div className="mb-2 block">
                    <Label htmlFor="label" value="Location" />
                  </div>
                  <TextInput
                    id="location"
                    placeholder="Location"
                    // value={name}
                    name="location"
                    // onChange={(event) => setlabel(event.target.value)}
                    required
                  />
                </div>
                <div>
                  <div className="mb-2 block">
                    <Label htmlFor="label" value="Logo" />
                  </div>

                  <input
                    type="file"
                    id="logo"
                    name="logo"
                    accept="image/*"
                    //   cursor="pointer"

                    disabled
                    className="cursor-not-allowed"
                  />
                </div>

                <div className="w-full">
                  <button
                    type="submit"
                    class="w-full rounded-lg bg-primary-600 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-primary-700 focus:outline-none focus:ring-4 focus:ring-primary-300 dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
                  >
                    Add New company
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

export default Companies;
