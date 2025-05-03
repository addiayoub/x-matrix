import React, { useState } from "react";
import { IoAddCircleOutline } from "react-icons/io5";
import ImprovementPrioritiesModal from "./molecules/ImprovementPrioritiesModal";
import AnnualObjectiveModal from "./molecules/AnnualObjectiveModal";
import ImplementationTacticsModal from "./molecules/ImplementationTacticsModal";
import StrategicObjectiveModal from "./molecules/StrategicObjectiveModal";
import { addIp, fetchIps } from "../services/ip";
import { addSo, fetchSos } from "../services/so";
import { useParams } from "react-router-dom";
import { addAo, fetchAos } from "../services/ao";
import { addIt } from "../services/it";

const sections = [
  { name: "Improvement projects", modal: "improvement", top: "15%", left: "50%" },
  { name: "Annual objectives", modal: "annual", top: "50%", left: "20%" },
  { name: "KPIs", modal: "implementation", top: "50%", left: "80%" },
  { name: "Long term objectives", modal: "strategic", top: "85%", left: "50%" },
];

const GeometricSections = ({ refreshMatrix }) => {
  const { id } = useParams();
  const [openModal, setOpenModal] = useState(null); // Track which modal is open
  const [selectedData, setSelectedData] = useState(null);

  const handleAdd = (modalType) => {
    setSelectedData(null);
    setOpenModal(modalType);
    console.log(`Open modal: ${modalType}`);
  };

  const addIps = async (data) => {
    try {
      if (!data) {
        throw new Error("Invalid data for Improvement Priority");
      }
      console.log("Adding Improvement Priority:", data);
  
      const response = await addIp(data);
      console.log("Response from addIp:", response);
  
      refreshMatrix();
    } catch (error) {
      console.error("Error adding Improvement Priority:", error.message || error);
  
      // Optional: Handle specific error scenarios
      if (error.response) {
        console.error("API Error Response:", error.response.data);
      }
    }
  };
  const addSos = async (data) => {
    try {
      if (!data || !id) {
        throw new Error("Invalid data or missing xMatrixId");
      }
      console.log("Adding Strategic Objective:", data);
      const response = await addSo({ ...data, xMatrixId: id });
      console.log("Response from addSo:", response);
      refreshMatrix();
    } catch (error) {
      console.error("Error adding Strategic Objective:", error.message || error);
    }
  };
  
  const addAos = async (data) => {
    try {
      if (!data) {
        throw new Error("Invalid data for Annual Objective");
      }
      console.log("Adding Annual Objective:", data);
      const response = await addAo(data);
      console.log("Response from addAo:", response);
      refreshMatrix();
    } catch (error) {
      console.error("Error adding Annual Objective:", error.message || error);
    }
  };
  
  const addIts = async (data) => {
    try {
      if (!data) {
        throw new Error("Invalid data for Implementation Tactic");
      }
      console.log("Adding Implementation Tactic:", data);
      const response = await addIt(data);
      console.log("Response from addIt:", response);
      refreshMatrix();
    } catch (error) {
      console.error("Error adding Implementation Tactic:", error.message || error);
    }
  };
  return (
    <div className="w-full h-full relative">
      <svg viewBox="0 0 100 100" preserveAspectRatio="none" className="w-full h-full absolute top-0 left-0">
        <rect x="0" y="0" width="100" height="100" fill="white" />
        <polygon points="0,0 50,50 0,100" className="fill-gray-200" />
        <polygon points="0,0 50,50 100,0" className="fill-gray-400" />
        <polygon points="100,0 50,50 100,100" className="fill-gray-300" />
        <polygon points="0,100 50,50 100,100" className="fill-gray-500" />
        <line x1="0" y1="0" x2="100" y2="100" stroke="white" strokeWidth="2" />
        <line x1="100" y1="0" x2="0" y2="100" stroke="white" strokeWidth="2" />
      </svg>

      {/* Render section buttons dynamically */}
      {sections.map(({ name, modal, top, left }) => (
        <div
          key={name}
          className="absolute text-xs text-center max-w-full truncate cursor-pointer flex items-center justify-center gap-1"
          style={{ top, left, transform: "translate(-50%, -50%)" }}
          onClick={() => handleAdd(modal)}
        >
          <span>{name}</span> <IoAddCircleOutline />
        </div>
      ))}

      {/* Render modals conditionally */}
      {openModal === "improvement" && (
        <ImprovementPrioritiesModal
          isOpen={true}
          onClose={() => setOpenModal(null)}
          onSubmit={addIps}
          initialData={selectedData}
        />
      )}
      {openModal === "annual" && (
        <AnnualObjectiveModal
          isOpen={true}
          onClose={() => setOpenModal(null)}
          onSubmit={addAos}
          initialData={selectedData}
        />
      )}
      {openModal === "implementation" && (
        <ImplementationTacticsModal
          isOpen={true}
          onClose={() => setOpenModal(null)}
          onSubmit={addIts}
          initialData={selectedData}
        />
      )}
      {openModal === "strategic" && (
        <StrategicObjectiveModal
          isOpen={true}
          onClose={() => setOpenModal(null)}
          onSubmit={addSos}
          initialData={selectedData}
        />
      )}
    </div>
  );
};

export default GeometricSections;
