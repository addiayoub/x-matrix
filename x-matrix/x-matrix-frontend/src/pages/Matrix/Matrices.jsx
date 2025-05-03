import React, { useEffect } from "react";
import { IoMdAdd } from "react-icons/io";
import { Button, Checkbox, Label, Modal, TextInput } from "flowbite-react";
import { useState } from "react";
import { Link } from "react-router-dom";
import api from "../../services/api";
import { addMatrix, fetchMatrices } from "../../services/matrix";
const Matrices = () => {
  const [openModal, setOpenModal] = useState(false);
    const [matrices, setMatrices] = useState([]);


    useEffect(() => {
      const loadMatrices = async () => {
        const data = await fetchMatrices();
        if (data) setMatrices(data);
        console.log("Fetched matrices:", data);
      };
      loadMatrices();
    }, []);

  function onCloseModal() {
    setOpenModal(false);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData);
    console.log(data);

    const response = await addMatrix(data); 
    console.log("matrix response", response);
    if  (response.status >= 200 && response.status < 300) {
      console.log("Matrix created successfully:", response.data);
      const updatedMatrices = await fetchMatrices();
      setMatrices(updatedMatrices); 
      setOpenModal(false);
      alert("Matrix created successfully!");
    }
    else {
      const errorMessage = response?.data || "An error occurred while creating the matrix.";
      console.error("Error creating matrix:", errorMessage);
      alert(errorMessage);  


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
          Generate New Matrix
        </button>

        {/* table */}
       { 
        matrices  && Object.keys(matrices).length > 0
        
        ? (
          <div class="relative overflow-x-auto">
          <table class="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
            <thead class="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" class="px-6 py-3">
                  ID
                </th>
                <th scope="col" class="px-6 py-3">
                  Label
                </th>{" "}
                <th scope="col" class="px-6 py-3">
                  Generated Label
                </th>
                <th scope="col" class="px-6 py-3">
                  Added At
                </th>
                <th scope="col" class="px-6 py-3 text-right">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>


              {

                // matrices.map((matrix) => (
                  <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
                    <th
                      scope="row"
                      class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
                    >
                      {matrices.id}
                    </th>
                    <td class="px-6 py-4">{matrices.label}</td>{" "}
                    <td class="px-6 py-4">{matrices.generated_label || ""}</td>
                    <td class="px-6 py-4">{matrices.createdAt || ""}</td>
                    <td class="px-6 py-4 text-right">
                      <Link
                        class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                        to={`/matrices/show/${matrices.id}`}
                      >
                        View
                      </Link>
                    </td>
                  </tr>
                // ))
              }
             
            </tbody>
          </table>
        </div>
        ) : (
          <div className="flex justify-center items-center h-40">
            <p className="text-gray-500 dark:text-gray-400">No matrices found</p>
          </div>
        )
       }

        {/* modal */}

        <Modal show={openModal} size="md" onClose={onCloseModal} popup>
          <Modal.Header />
          <Modal.Body>
            <div className="space-y-6">
              <h3 className="text-xl font-medium text-gray-900 dark:text-white">
                Generate New Matrix
              </h3>
            <form action="" onSubmit={handleSubmit} className="flex gap-3 flex-col">
            <div>
                <div className="mb-2 block">
                  <Label htmlFor="label" value="Label" />
                </div>
                <TextInput
                  id="label"
                  placeholder="Label"
                  // value={label}
                  name="label"
                  // onChange={(event) => setlabel(event.target.value)}
                  required
                />
              </div>

              <div className="w-full">
              <button
                type="submit"
                class="w-full rounded-lg bg-primary-600 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-primary-700 focus:outline-none focus:ring-4 focus:ring-primary-300 dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
              >
                Add New Matrix
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

export default Matrices;
