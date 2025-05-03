import { Select } from "flowbite-react";
import React, { useState, useEffect } from "react";
import { Modal, Button, Label, TextInput, Datepicker } from "flowbite-react";
import { use } from "react";
import { useParams } from "react-router-dom";
import api from "../../services/api";
import { fetchHr } from "../../services/hr";

const ImplementationTacticsModal = ({ isOpen, onClose, onSubmit, initialData }) => {
  const [ips , setIps] = useState([]);
  const [hrs , setHrs] = useState([]);
  const { id } = useParams();
  const fetchIps = async () => {
    try {
      const response = await api.get(`/xmatrix/${id}/ips`);
      console.log(response.data);
      setIps(response.data);
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }
  };

   const [data, setData] = useState({
      label: "",
      start: "",
      end: "",
      ipId : "",
      resourceId : "",
    });
    const loadHr = async () => {
      console.log("id", id);
      const data = await fetchHr(id);
      console.log('hrs', data);
      if (data) setHrs(data);
    };
    useEffect(() => {
        fetchIps();
        
        loadHr();
        if (initialData) {
          setData({
            label: initialData.label || "",
            start: initialData.start ? new Date(initialData.start).toISOString().slice(0, 16) : "",
            end: initialData.end ? new Date(initialData.end).toISOString().slice(0, 16) : "",
            ipId: initialData.ipId || "",
            advancement: initialData.advancement || "",
            resourceId: initialData.resourceId || "",
          });
        } else {
          setData({
            label: "",
            start: "",
            end: "",
            ipId: "",
            advancement: "",
            resourceId: "",
          });
        }
      }, [initialData]);

      
        useEffect(() => {}, []);
      
        const handleSubmit = () => {
          onSubmit({
            label: data.label,
            start: data.start,
            end: data.end,
            ipId: data.ipId,
            advancement: data.advancement,
            resourceId: data.resourceId,
          });
          // clean form
      
          setData({
            label: "",
            start: "",
            end: "",
            ipId: "",
            advancement: "",
            resourceId: "",
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
                     ? "Edit Implementation Tactics"
                     : "Add New Implementation Tactics"}
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
                 <div>
                   <Label htmlFor="label" value="advancement" />
                   <TextInput
                     id="advancement"
                     type="number"
                     placeholder="advancement"
                     value={data.advancement}
                     onChange={(e) => setData({ ...data, advancement: e.target.value })}
                     required
                   />
                 </div>
                 <div className="flex flex-col gap-2">
                   <Label htmlFor="end" value="Improvement Priority" />
                   {/* <Datepicker  value={ endDate } onChange={(e) => setEndDate(e.target.value)} /> */}
                   <Select
                     id="ipId"
                     required
                     value={data.ipId}
                     onChange={(e) => setData({ ...data,  ipId: e.target.value })}
                     name="ipId"
                     disabled={!!initialData}
                   >
                     <option value="" disabled>
                       Select Improvement Priority
                     </option>
                     {ips.map((ip) => (
                       <option key={ip.id} value={ip.id} selected={ip.id == data.ipId}>
                         {ip.label}
                       </option>
                     ))}
                   </Select>
                 </div>
                 <div className="flex flex-col gap-2">
                   <Label htmlFor="end" value="Resource" />
                   {/* <Datepicker  value={ endDate } onChange={(e) => setEndDate(e.target.value)} /> */}
                   <Select
                     id="resourceId"
                     required
                     value={data.resourceId}
                     onChange={(e) => setData({ ...data,  resourceId: e.target.value })}
                     name="resourceId"
                   >
                     <option value="" disabled>
                       Select Resource
                     </option>
                     {hrs && hrs.map((hr) => (
                       <option key={hr.id} value={hr.id} selected={hr.id == data.resourceId}>
                         {hr.department}
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

export default ImplementationTacticsModal