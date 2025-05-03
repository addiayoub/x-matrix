import React, { useState, useEffect } from "react";
import { Modal, Button, Label, TextInput, Datepicker } from "flowbite-react";
import { DateInput, DateSegment } from "react-aria-components";

const StrategicObjectiveModal = ({ isOpen, onClose, onSubmit, initialData }) => {
  const [label, setLabel] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  useEffect(() => {
    if (initialData) {
      setLabel(initialData.label || "");
      setStartDate(initialData.start ? new Date(initialData.start).toISOString().slice(0, 16) : "");;
      setEndDate(initialData.end ? new Date(initialData.end).toISOString().slice(0, 16) : "");
    } else {
      setLabel("");
      setStartDate("");
      setEndDate("");
    }
  }, [initialData]);

  const handleSubmit = () => {
    onSubmit({ label, startDate, endDate });
    // clean form
    setLabel("");
    setStartDate("");
    setEndDate("");
    onClose();
  };

  return (
    <Modal show={isOpen} size="md" onClose={onClose} popup>
      <Modal.Header />
      <Modal.Body>
        <div className="space-y-6">
          <h3 className="text-xl font-medium text-gray-900 dark:text-white">
            {initialData ? "Edit Strategic Objective" : "Add New Strategic Objective"}
          </h3>
          <div>
            <Label htmlFor="label" value="Label" />
            <TextInput id="label" placeholder="Label" value={label} onChange={(e) => setLabel(e.target.value)} required />
          </div>
          <div className="flex flex-col gap-2 rounded-xl">
            <Label htmlFor="start" value="Start Date" />
            {/* <Datepicker value={ startDate } onChange={(e) => setStartDate(e.target.value)} /> */}
            <input type="datetime-local" name="start" id=""  value={startDate} onChange={(e) => setStartDate(e.target.value)} />
          </div>
          <div className="flex flex-col gap-2">
            <Label htmlFor="end" value="End Date" />
            {/* <Datepicker  value={ endDate } onChange={(e) => setEndDate(e.target.value)} /> */}
            <input type="datetime-local" name="end" id="" value={endDate} onChange={(e) => setEndDate(e.target.value)} />

          </div>
          <div className="w-full">
            <Button color="gray" onClick={handleSubmit}>
              {initialData ? "Update" : "Add"}
            </Button>
          </div>
        </div>
      </Modal.Body>
    </Modal>
  );
};

export default StrategicObjectiveModal;
