import React, { useEffect, useState } from "react";
import { CiImport } from "react-icons/ci";
import { FaEye } from "react-icons/fa";
import { IoMdAdd, IoMdArrowRoundBack } from "react-icons/io";
import { TiExport } from "react-icons/ti";
import { Link, useParams } from "react-router-dom";
import Back from "../../components/atoms/Back";
import api from "../../services/api";
import { fetchMatrix } from "../../services/matrix";
import { Button, Checkbox, FileInput, Label, Modal, ModalBody, ModalHeader, TextInput } from "flowbite-react";
const ShowMatrix = () => {
  const { id } = useParams();

  const [matrix, setMatrix] = useState({});
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    const loadMatrix = async () => {
      try {
        const data = await fetchMatrix(id);
        if (data) setMatrix(data);
        setLoading(false);
      }
      catch (error) {
        console.error("Error fetching matrix:", error);

      }
    };
    loadMatrix();
  }, []);
  const [openModal, setOpenModal] = useState(false);
  const [file, setFile] = useState(null);
  const [email, setEmail] = useState("");

  function onCloseModal() {
    setOpenModal(false);
    setEmail("");
  }
  const handleExport = async () => {
    console.log("Exporting matrix with ID:", id);
    try {
      const response = await api.get(`/xmatrix/${matrix?.id}/export`, {
        responseType: "blob",
        headers: {
          "Content-Type": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        },
      });

      const blob = new Blob([response.data], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" });
      
      const link = document.createElement("a");
      link.href = window.URL.createObjectURL(blob);
      link.download = `matrix_${matrix?.id}.xlsx`; 
      link.click();

      alert("Matrix exported successfully!");
    } catch (error) {
      console.error("Error exporting matrix:", error);
      alert("Failed to export matrix. Please try again.");
    }
};

const [importLoading, setImportLoading] = useState(false);

  const handleImport = async () => {
    if (!file) {
      alert("Please select a file to import.");
      return;
    }
    
    const formData = new FormData();
    formData.append("file", file);
    console.log("Form data:", formData.get("file"));
    setImportLoading(true);

    try {
      const response = await api.post(`/xmatrix/${matrix?.id}/upload`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      
      alert("Matrix imported successfully!");
      console.log("Import Response:", response.data);
      setOpenModal(false);

    } catch (error) {
      console.error("Error importing matrix:", error);
      alert("Failed to import matrix. Please try again.");
      setImportLoading(false);
    }
  };
  return (
    <div className="flex flex-col gap-5">
      <Back path="/matrices" />
      <div className="flex flex-row justify-between">
        <h1>Matrix : [ Generated Label ]  &nbsp;
          {matrix?.createdAt }
        </h1>

        <div className="flex flex-row gap-2">
          <button
            type="button"
            class=" mb-2 me-2 flex w-fit  flex-row  items-center gap-2 bg-[#3b5998] px-5  py-2.5  text-center text-sm font-medium text-white hover:bg-[#3b5998]/90"
            onClick={() => handleExport()}
          >
            <TiExport />
            Export
          </button>
          <button
            type="button"
            class=" mb-2 me-2 flex w-fit  flex-row  items-center gap-2 bg-[#3b5998] px-5  py-2.5  text-center text-sm font-medium text-white hover:bg-[#3b5998]/90"
            onClick={() => setOpenModal(true)}
          >
            <CiImport />
            Import
          </button>
          <Link
            type="button"
            to={`/generated-matrix/${matrix?.id}`}
            class=" mb-2 me-2 flex w-fit  flex-row  items-center gap-2 bg-[#3b5998] px-5  py-2.5  text-center text-sm font-medium text-white hover:bg-[#3b5998]/90"
          >
            <FaEye />
            View Schema
          </Link>
        </div>
      </div>

      {/* table */}
      <div class="relative overflow-x-auto">
        <table class="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
          <thead class="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th scope="col" class="px-6 py-3">
                Managament
              </th>
             
              <th scope="col" class="px-6 py-3 text-right">
                Actions
              </th>
            </tr>
          </thead>
          <tbody>
            <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
              <th
                scope="row"
                class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
              >
                Strategic Objectives
              </th>
             
              <td class="px-6 py-4 text-right">
                <Link
                  class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                  to={`/strategic-objectives/${id}`}
                >
                  View
                </Link>
              </td>
            </tr>
            <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
              <th
                scope="row"
                class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
              >
                Annual Objectives
              </th>
             
              <td class="px-6 py-4 text-right">
                <Link
                  class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                  to={`/annual-objectives/${id}`}
                >
                  View
                </Link>
              </td>
            </tr>
            <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
              <th
                scope="row"
                class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
              >
                Implementation Plans
              </th>
           
              <td class="px-6 py-4 text-right">
                <Link
                  class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                  to={`/improvement-priorities/${id}`}
                >
                  View
                </Link>
              </td>
            </tr>

            <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
              <th
                scope="row"
                class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
              >
                Implementation Tasks
              </th>
           
              <td class="px-6 py-4 text-right">
                <Link
                  class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                  to={`/implementation-tasks/${id}`}
                >
                  View
                </Link>
              </td>
            </tr>

            <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
              <th
                scope="row"
                class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
              >
                Resources
              </th>
            
           
              <td class="px-6 py-4 text-right">
                <Link
                  class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                  to={`/resources/${id}`}
                >
                  View
                </Link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <Modal show={openModal} size="md" onClose={onCloseModal} popup>
        <ModalHeader />
        <ModalBody>
          <div className="flex flex-col gap-5">
            {/* <h3 className="text-xl font-medium text-gray-900 dark:text-white">Import Matrix</h3> */}
            <form action=""  className="flex flex-col gap-5"> 
            <div>
            <Label className="mb-2 block" htmlFor="file-upload">
        Upload file
      </Label>
      <FileInput id="file-upload"
       
        onChange={(e) => setFile(e.target.files[0])}
        // helperText="Upload your file here"
        required
      />
            </div>
            
        
            <div className="w-full">
              <Button color="gray" onClick={() => handleImport()} className="w-full">
              {importLoading ? "Importing..." : "Import"}
                                
                                </Button>
            </div>
            </form>
          
          </div>
        </ModalBody>
      </Modal>
    </div>
  );
};

export default ShowMatrix;
