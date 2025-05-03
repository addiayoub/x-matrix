import React, { useEffect, useState } from 'react'
import Back from '../../components/atoms/Back'
import { IoMdAdd } from 'react-icons/io'
import { useParams } from 'react-router-dom';
import api from '../../services/api';
import { FaEdit } from 'react-icons/fa';
import ImplementationTacticsModal from '../../components/molecules/ImplementationTacticsModal';
import { addIt, fetchIts, updateIt } from '../../services/it';
import { formatToTwoDecimalPlaces } from '../../utils';

const Its = () => {
    const { id } = useParams();
    const [openModal, setOpenModal] = useState(false);
    const [selectedData, setSelectedData] = useState(null);
    const [its , setIts] = useState([]);
  
    useEffect(() => {
      const loadIts = async () => {
        const data = await fetchIts(id);
        if (data) setIts(data);
      };
      
      loadIts();
    }, [id]);
  
    const handleAdd =  () => {
      setSelectedData(null);
      setOpenModal(true);
      console.log("Add new strategic objective" , selectedData);
      // try{
      //   const response = await api.post("sos", data);
      // }catch(error){
      //   console.error("Error adding objective:", error);
      // }
    };
  
    const handleEdit = (data) => {
      setSelectedData(data);
      setOpenModal(true);
    };
  
    const addObjective = async (data) => {
      console.log("Add new strategic objective", data);
      await addIt(data);
      const updatedSos = await fetchIts(id);
      if (updatedSos) setIts(updatedSos);
      setSelectedData(null);
    };
    const updateObjective = async (data) => {
      console.log("Update strategic objective", data);
      console.log("Selected data id", selectedData.id);
      await updateIt({...data, itId: selectedData.id});
      const updatedSos = await fetchIts(id);
      if (updatedSos) setIts(updatedSos); 
      setSelectedData(null);
      setOpenModal(false);
    };
  return (
    <div className="flex flex-col gap-5">
    <Back path={`/matrices/show/${id}`} />
   <button
     onClick={handleAdd}
     type="button"
     className="mb-2 me-2 flex w-fit flex-row items-center gap-2 bg-[#3b5998] px-5 py-2.5 text-sm font-medium text-white hover:bg-[#3b5998]/90"
   >
     <IoMdAdd />
     Add New Improvement Target
   </button>

   {

     its.length > 0 ? (
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
               advancement (%)
             </th>{" "}
             <th scope="col" class="px-6 py-3">
               Period Length (Days)
             </th>{" "}
             <th scope="col" class="px-6 py-3">
               Time Spend (Days)
             </th>
             <th scope="col" class="px-6 py-3">
               Trend
             </th>
             <th scope="col" class="px-6 py-3">
               Progress Time (%)
             </th>
             <th scope="col" class="px-6 py-3">
               Related IP
             </th>
             <th scope="col" class="px-6 py-3">
               Related HR
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
             its && its.map((it) => (

               <tr class="border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
             <th
               scope="row"
               class="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
             >
               {it.id}
             </th>
             <td class="px-6 py-4">{it.label}</td>{" "}
             <td class="px-6 py-4">{it.start}</td>{" "}
             <td class="px-6 py-4">{it.end}</td>{" "}
             <td class="px-6 py-4">{formatToTwoDecimalPlaces(it.advancement)}</td>{" "}
             <td class="px-6 py-4">{it.periodLength}</td>{" "}
             <td class="px-6 py-4">{it.timeSpent}</td>
             <td class="px-6 py-4">{it.trend}</td>
             <td class="px-6 py-4">{formatToTwoDecimalPlaces(it.progressTime)}</td>
             <td class="px-6 py-4">{it.ipId}</td>
             <td class="px-6 py-4">{it.resourceId}</td>
             {/* <td class="px-6 py-4">{it.createdAt}</td> */}
             <td class="flex flex-row justify-end gap-2 px-6 py-4 text-right">
              
               <button
                 class="font-medium text-blue-600 hover:underline dark:text-blue-500"
                 onClick={() => handleEdit(it)}
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
       <p>No annual objectives found</p>
     )
   }

   <ImplementationTacticsModal
     isOpen={openModal}
     onClose={() => setOpenModal(false)}
     onSubmit={selectedData ? updateObjective : addObjective}
     initialData={selectedData}
   />
 </div>
  )
}

export default Its