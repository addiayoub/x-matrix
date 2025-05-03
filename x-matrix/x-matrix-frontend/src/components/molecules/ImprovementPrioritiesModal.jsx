import { Select } from "flowbite-react";
import React, { useState, useEffect } from "react";
import { Modal, Button, Label, TextInput, Datepicker } from "flowbite-react";
import { use } from "react";
import { useParams } from "react-router-dom";
import api from "../../services/api";
const ImprovementPrioritiesModal = ({ isOpen, onClose, onSubmit, initialData }) => {
  const [aos , setAos] = useState([]);
  const { id } = useParams();
  const fetchAos = async () => {
    try {
      const response = await api.get(`/xmatrix/${id}/aos`);
      console.log(response.data);
      setAos(response.data);
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }
  };
  const [data, setData] = useState({
    label: "",
    start: "",
    end: "",
    aoId : "",
  });
  useEffect(() => {
    fetchAos();
    if (initialData) {
      setData({
        label: initialData.label || "",
        start: initialData.start ? new Date(initialData.start).toISOString().slice(0, 16) : "",
        end: initialData.end ? new Date(initialData.end).toISOString().slice(0, 16) : "" ,
        aoId: initialData.aoId || "",
      });
    } else {
      setData({
        label: "",
        start: "",
        end: "",
        aoId: "",
      });
    }
  }, [initialData]);

  useEffect(() => {}, []);

  const handleSubmit = () => {
    onSubmit({
      label: data.label,
      start: data.start,
      end: data.end,
      aoId: data.aoId,
    });
    // clean form

    setData({
      label: "",
      start: "",
      end: "",
      aoId: "",
    });
    onClose();
  };
  return (
    <Modal show={isOpen} size="md" onClose={onClose} popup>
         <Modal.Header />
         <Modal.Body>
           <div className="space-y-6">
             <h3 className="text-xl font-medium text-gray-900 dark:text-white">
               {initialData
                 ? "Edit Improvement Priorities"
                 : "Add New Improvement Priorities"}
             </h3>
             <div>
               <Label htmlFor="label" value="Label" />
               <TextInput
                 id="label"
                 placeholder="Label"
                 value={data.label}
                 onChange={(e) => setData({ ...data, label: e.target.value })}
                 required
               />
             </div>
             <div className="flex flex-col gap-2">
               <Label htmlFor="end" value="Annual Objective" />
               {/* <Datepicker  value={ endDate } onChange={(e) => setEndDate(e.target.value)} /> */}
               <Select
                 id="aoId"
                 required
                 value={data.aoId}
                 onChange={(e) => setData({ ...data,  aoId: e.target.value })}
                 name="aoId"
                 disabled={!!initialData}

               >
                 <option value="" disabled>
                   Select Annual Objective
                 </option>
                 {aos.map((ao) => (
                   <option key={ao.id} value={ao.id} selected={ao.id == data.aoId}>
                     {ao.label}
                   </option>
                 ))}
               </Select>
             </div>
             <div className="flex flex-col gap-2 rounded-xl">
               <Label htmlFor="start" value="Start Date" />
               {/* <Datepicker value={ startDate } onChange={(e) => setStartDate(e.target.value)} /> */}
               <input
                 type="datetime-local"
                 name="start"
                 id=""
                 value={data.start}
                 onChange={(e) => setData({ ...data, start: e.target.value })}
               />
             </div>
             <div className="flex flex-col gap-2">
               <Label htmlFor="end" value="End Date" />
               {/* <Datepicker  value={ endDate } onChange={(e) => setEndDate(e.target.value)} /> */}
               <input
                 type="datetime-local"
                 name="end"
                 id=""
                 value={data.end}
                 onChange={(e) => setData({ ...data, end: e.target.value })}
               />
             </div>
             <div className="w-full">
               <Button color="gray" onClick={handleSubmit}>
                 {initialData ? "Update" : "Add"}
               </Button>
             </div>
           </div>
         </Modal.Body>
       </Modal>
  )
}

export default ImprovementPrioritiesModal