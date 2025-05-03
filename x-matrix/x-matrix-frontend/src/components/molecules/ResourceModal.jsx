import React, { useState, useEffect } from "react";
import { Modal, Button, Label, TextInput, Datepicker } from "flowbite-react";
import { DateInput, DateSegment } from "react-aria-components";

const ResourceModal = ({ isOpen, onClose, onSubmit, initialData }) => {
   const [department, setDepartment] = useState("");
  
  
    useEffect(() => {
      if (initialData) {
        setDepartment(initialData.department || "");
       
      } else {
        setDepartment("");
       
      }
    }, [initialData]);
  
    const handleSubmit = () => {
      onSubmit({ department });
   
      setDepartment("");
    
      onClose();
    };
  return (
    <Modal show={isOpen} size="md" onClose={onClose} popup>
          <Modal.Header />
          <Modal.Body>
            <div className="space-y-6">
              <h3 className="text-xl font-medium text-gray-900 dark:text-white">
                {initialData ? "Edit Resource" : "Add New Resource"}
              </h3>
              <div>
                <Label htmlFor="department" value="Department" />
                <TextInput id="department" placeholder="department" value={department} onChange={(e) => setDepartment(e.target.value)} required />
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

export default ResourceModal