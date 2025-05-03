import React, { useEffect, useState } from "react";
import { IoMdAdd } from "react-icons/io";
import { FaEdit, FaEye } from "react-icons/fa";
import Back from "../../components/atoms/Back";
import StrategicObjectiveModal from "../../components/molecules/StrategicObjectiveModal";
import { Link, useParams } from "react-router-dom";
import { useDispatch } from "react-redux";
import api from "../../services/api";
import { use } from "react";
import { addSo, fetchSos } from "../../services/so";
import { formatToTwoDecimalPlaces } from "../../utils";
import { addHr, fetchHr, updateHr } from "../../services/hr";
import ResourceModal from "../../components/molecules/ResourceModal";

const Hrs = () => {

    const { id } = useParams();
      const [openModal, setOpenModal] = useState(false);
      const [selectedData, setSelectedData] = useState(null);
      const [hrs , setHrs] = useState([]);
    
    
      useEffect(() => {
        const loadHr = async () => {
          console.log("id", id);
          const data = await fetchHr(id);
          if (data) setHrs(data);
        };
        loadHr();
      }, [id]);
    
      const handleAdd =  () => {
        setSelectedData(null);
        setOpenModal(true);
        console.log("Add new strategic objective" , selectedData);
      };
    
      const handleEdit = (data) => {
        setSelectedData(data);
        setOpenModal(true);
      };
    
      
      const updateObjective = async (data) => {
        console.log("Update strategic objective", data);
        console.log("selectedData", selectedData);
        
        await updateHr({ ...data ,resourceId: selectedData.id});
        const updatedHrs = await fetchHr(id);
        if (updatedHrs) setHrs(updatedHrs);
        setSelectedData(null);
      };
      const addObjective = async (data) => {
        console.log("Add new strategic objective", data);
        console.log("xMatrixId", id);
        
        await addHr({ ...data });
        const updatedHrs = await fetchHr(id);
        if (updatedHrs) setHrs(updatedHrs);
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
        Add New Resource
      </button>

      {

        hrs.length > 0 ? (
          <div className="relative overflow-x-auto">
          <table class="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
            <thead class="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" class="px-6 py-3">
                  ID
                </th>
                <th scope="col" class="px-6 py-3">
                  Department
                </th>{" "}
               
                <th scope="col" class="px-6 py-3">
                  Actual Progress (%)
                </th>{" "}
                <th scope="col" class="px-6 py-3">
                  Timely Progress (%)
                </th>{" "}

                <th scope="col" class="px-6 py-3">
                  Trend
                </th>{" "}
               
                <th scope="col" class="px-6 py-3 text-right">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>
              {
                hrs && hrs.map((hr) => (
  
                  <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
                <th
                  scope="row"
                  class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
                >
                  {hr.id}
                </th>
                <td class="px-6 py-4">{hr.department}</td>{" "}
                <td class="px-6 py-4">{hr.actualProgress}</td>{" "}
                <td class="px-6 py-4">{formatToTwoDecimalPlaces(hr.timelyProgress)}</td>{" "}
                <td class="px-6 py-4">{hr.trend}</td>{" "}
              
                {/* <td class="px-6 py-4">{formatToTwoDecimalPlaces(hr.progressTime)}</td> */}
                <td class="flex flex-row justify-end gap-2 px-6 py-4 text-right">
                 
                  <button
                    class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                    onClick={() => handleEdit(hr)}
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
          <p>No Resources found</p>
        )
      }

      <ResourceModal
        isOpen={openModal}
        onClose={() => setOpenModal(false)}
        onSubmit={selectedData ? updateObjective : addObjective}
        initialData={selectedData}
      />
    </div>
  )
}

export default Hrs