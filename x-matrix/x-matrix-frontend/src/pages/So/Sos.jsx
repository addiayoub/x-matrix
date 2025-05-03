import React, { useEffect, useState } from "react";
import { IoMdAdd } from "react-icons/io";
import { FaEdit, FaEye } from "react-icons/fa";
import Back from "../../components/atoms/Back";
import StrategicObjectiveModal from "../../components/molecules/StrategicObjectiveModal";
import { Link, useParams } from "react-router-dom";
import { useDispatch } from "react-redux";
import api from "../../services/api";
import { use } from "react";
import { addSo, fetchSos, updateSo } from "../../services/so";
import { formatToTwoDecimalPlaces } from "../../utils";

const Sos = () => {

  const { id } = useParams();
  const [openModal, setOpenModal] = useState(false);
  const [selectedData, setSelectedData] = useState(null);
  const [sos, setSos] = useState([]);


  useEffect(() => {
    const loadSos = async () => {
      const data = await fetchSos(id);
      if (data) setSos(data);
    };
    loadSos();
  }, [id]);

  const handleAdd =  () => {
    setSelectedData(null);
    setOpenModal(true);
    console.log("Add new strategic objective" , selectedData);
  };

  const handleEdit = (data) => {
    console.log("Edit strategic objective", data);
    setSelectedData(data);
    setOpenModal(true);
  };

  
  const updateObjective = async (data) => {
    console.log("Update strategic objective", data);
    console.log("Id", selectedData.id);
    await updateSo({ ...data, soId: selectedData.id });
    console.log("xMatrixId", id);
    const updatedSos = await fetchSos(id);
    if (updatedSos) setSos(updatedSos);
    setSelectedData(null);
    setOpenModal(false);
  };
  const addObjective = async (data) => {
    console.log("Add new strategic objective", data);
    console.log("xMatrixId", id);
    await addSo({ ...data, xMatrixId: id });
    const updatedSos = await fetchSos(id);
    if (updatedSos) setSos(updatedSos);
    setSelectedData(null);
  };
  return (
    <div className="flex flex-col gap-5">
      <Back path={`/matrices/show/${id}`}
       />
      <button
        onClick={handleAdd}
        type="button"
        className="mb-2 me-2 flex w-fit flex-row items-center gap-2 bg-[#3b5998] px-5 py-2.5 text-sm font-medium text-white hover:bg-[#3b5998]/90"
      >
        <IoMdAdd />
        Add New Strategic Objective
      </button>

      {

        sos.length > 0 ? (
          <div className="relative overflow-x-auto">
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
                  Start Date
                </th>{" "}
                <th scope="col" class="px-6 py-3">
                  End Date
                </th>{" "}
                <th scope="col" class="px-6 py-3">
                  advancement %
                </th>{" "}
                <th scope="col" class="px-6 py-3">
                  Period Length (Days)
                </th>{" "}
                <th scope="col" class="px-6 py-3">
                  Time Spent (Days)
                </th>
                <th scope="col" class="px-6 py-3">
                  Trend
                </th>
                <th scope="col" class="px-6 py-3">
                  Progress Time %
                </th>
               
                {/* <th scope="col" class="px-6 py-3">
                  Added At
                </th> */}
                <th scope="col" class="px-6 py-3 text-right">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>
              {
                sos && sos.map((so) => (
  
                  <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
                <th
                  scope="row"
                  class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
                >
                  {so.id}
                </th>
                <td class="px-6 py-4">{so.label}</td>{" "}
                <td class="px-6 py-4">{so.start}</td>{" "}
                <td class="px-6 py-4">{so.end}</td>{" "}
                <td class="px-6 py-4">{formatToTwoDecimalPlaces(so.advancement)}</td>{" "}
                <td class="px-6 py-4">{so.periodLength}</td>{" "}
                <td class="px-6 py-4">{so.timeSpent}</td>
                <td class="px-6 py-4">{so.trend}</td>
                <td class="px-6 py-4">{formatToTwoDecimalPlaces(so.progressTime)}</td>
                {/* <td class="px-6 py-4">{so.createdAt}</td> */}
                <td class="flex flex-row justify-end gap-2 px-6 py-4 text-right">
                 
                  <button
                    class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                    onClick={() => handleEdit(so)}
                  >
                    <FaEdit />
                  </button>
                </td>
              </tr>
  
                ))
  
              }
            </tbody>
          </table>
        </div>
        ) : (
          <p>No strategic objectives found</p>
        )
      }

      <StrategicObjectiveModal
        isOpen={openModal}
        onClose={() => setOpenModal(false)}
        onSubmit={selectedData ? updateObjective : addObjective}
        initialData={selectedData}
      />
    </div>
  );
};

export default Sos;
