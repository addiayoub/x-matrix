import { Select } from "flowbite-react";
import React, { useState, useEffect } from "react";
import { Modal, Button, Label, TextInput, Datepicker } from "flowbite-react";
import { use } from "react";
import { useParams } from "react-router-dom";
import api from "../../services/api";

const AnnualObjectiveModal = ({ isOpen, onClose, onSubmit, initialData }) => {
  const [sos, setSos] = useState([]);
  const { id } = useParams();
  const fetchSos = async () => {
    try {
      const response = await api.get(`/xmatrix/${id}/sos`);
      console.log(response.data);
      setSos(response.data);
    } catch (error) {
      console.error("Error fetching strategic objectives:", error);
    }
  };
  const [data, setData] = useState({
    label: "",
    start: "",
    end: "",
    soId: "",
  });
  useEffect(() => {
    fetchSos();
    if (initialData) {
      setData({
        label: initialData.label || "",
        start: initialData.start ? new Date(initialData.start).toISOString().slice(0, 16) : "",
        end: initialData.end ? new Date(initialData.end).toISOString().slice(0, 16) : "",
        soId: initialData.soId || "",
      });
    } else {
      setData({
        label: "",
        start: "",
        end: "",
        soId: "",
      });
    }
  }, [initialData]);

  useEffect(() => {}, []);

  const handleSubmit = () => {
    onSubmit({
      label: data.label,
      start: data.start,
      end: data.end,
      soId: data.soId,
    });
    // clean form

    setData({
      label: "",
      start: "",
      end: "",
      soId: "",
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
              ? "Edit Annual Objective"
              : "Add New Annual Objective"}
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
            <Label htmlFor="end" value="Strategic Objective" />
            {/* <Datepicker  value={ endDate } onChange={(e) => setEndDate(e.target.value)} /> */}
            <Select
              id="soId"
              required
              value={data.soId}
              onChange={(e) => setData({ ...data, soId: e.target.value })}
              name="soId"
              disabled={!!initialData}
            >
              <option value="" disabled>
                Select Strategic Objective
              </option>
              {sos.map((so) => (

                <option key={so.id} value={so.id} selected={so.id == data.soId}>
                  {so.label}
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
  );
};

export default AnnualObjectiveModal;
