import React, { useEffect, useState } from "react";

import {
  MdCircle,
  MdEditSquare,
  MdLocalPrintshop,
  MdModeEditOutline,
} from "react-icons/md";

import { Button, Modal } from "flowbite-react";
import DataTable from "../../components/DataTable";
import DataTable2 from "../../components/DataTable2";
import DataTableRotation from "../../components/DataTableRotation";
import CircleIndicator from "../../components/CircleIndicator";
import GeometricSections from "../../components/GeometricSections";
import IconWrapper from "../../components/IconWrapper";
import {
  extractMatrixData,
  formatToTwoDecimalPlaces,
  getCharFromValue,
  getTailwindColor,
} from "../../utils";
import { use } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../services/api";
import { fetchSos, updateSo } from "../../services/so";
import { addHr, fetchHr } from "../../services/hr";
import StrategicObjectiveModal from "../../components/molecules/StrategicObjectiveModal";
import AnnualObjectiveModal from "../../components/molecules/AnnualObjectiveModal";
import { updateAo } from "../../services/ao";
import { updateIp } from "../../services/ip";
import { updateIt } from "../../services/it";
import ImplementationTacticsModal from "../../components/molecules/ImplementationTacticsModal";
import ImprovementPrioritiesModal from "../../components/molecules/ImprovementPrioritiesModal";
import ResourceModal from "../../components/molecules/ResourceModal";
import { IoAddCircleOutline, IoArrowBack } from "react-icons/io5";
import Logo from "../../components/atoms/Logo";
import Trend from "../../components/atoms/Trend";
const GeneratedMatrix = () => {
  const navigate = useNavigate();
  const [data, setData] = useState({});
  const [selectedRow, setSelectedRow] = useState({});
  const [loading, setLoading] = useState(true);
  const [openModal, setOpenModal] = useState(false);
  const [selectedData, setSelectedData] = useState(null);
  const [matrix, setMatrix] = useState({
    sos: [],
    aos: [],
    ips: [],
    its: [],
  });
  const { id } = useParams();

  const [sosList, setSosList] = useState([]);
  const [aosList, setAosList] = useState([]);
  const [ipsList, setIpsList] = useState([]);
  const [itsList, setItsList] = useState([]);
  const [resources, setResources] = useState([]);

  const fetchMatrix = async () => {
    try {
      const response = await api.get(`/xmatrix/${id}`);
      setMatrix(response.data); // Update the matrix state
      setLoading(false);
      console.log("Matrix", response.data);
    } catch (error) {
      console.error("Error fetching matrix: ", error);
    }
  };

  const fetchResources = async () => {
    try {
      const response = await fetchHr();
      setResources(response);
      console.log("Resources", response);
    } catch (error) {
      console.error("Error fetching resources: ", error);
    }
  };

  useEffect(() => {
    fetchMatrix();
    fetchResources();
  }, [id]);
  useEffect(() => {
    if (matrix && Object.keys(matrix).length > 0) {
      const extractedData = extractMatrixData(matrix);
      setSosList(extractedData.sos);
      setAosList(extractedData.aos);
      setIpsList(extractedData.ips);
      setItsList(extractedData.its);
    }
  }, [matrix]);
  const handleEdit = (data, entity) => {
    console.log("Edit strategic objective", data);
    setSelectedData(data);
    setOpenModal(entity);
  };

  const handleAdd = (entity) => {
    setSelectedData(null);
    setOpenModal(entity);
    console.log("Add new strategic objective", selectedData);
  };

  const updateSos = async (data) => {
    console.log("Update strategic objective", data);
    console.log("Id", selectedData.id);
    await updateSo({ ...data, soId: selectedData.id });
    console.log("xMatrixId", id);
    fetchMatrix();
    setSelectedData(null);
    setOpenModal(false);
  };

  const updateAos = async (data) => {
    console.log("Update strategic objective", data);
    await updateAo({ ...data, aoId: selectedData.id });
    // const updatedSos = await fetchAos(id);
    // if (updatedSos) setAos(updatedSos);
    fetchMatrix();
    setSelectedData(null);
  };

  const updateIps = async (data) => {
    console.log("Update strategic objective", data);
    // console.log("Selected data", selectedData);
    console.log("Selected data id", selectedData.id);
    await updateIp({ ...data, ipId: selectedData.id });
    fetchMatrix();
    setSelectedData(null);
    setOpenModal(false);
  };
  const updateIts = async (data) => {
    console.log("Update strategic objective", data);
    console.log("Selected data id", selectedData.id);
    await updateIt({ ...data, itId: selectedData.id });
    fetchMatrix();
    setSelectedData(null);
    setOpenModal(false);
  };

  const updateHrs = async (data) => {
    console.log("Add new strategic objective", data);
    console.log("xMatrixId", id);

    await addHr({ ...data });
    fetchMatrix();
    setSelectedData(null);
  };

  const addHrs = async (data) => {
    console.log("Add new strategic objective", data);
    console.log("xMatrixId", id);
    const response = await addHr({ ...data });
    console.log("Response from addHrs:", response);
    fetchMatrix();
    setSelectedData(null);
  };

  const filteredResources = resources
    ? resources.filter((resource) =>
        itsList.some((it) => it["resourceId"] == resource["id"]),
      )
    : [];
  const handlePrint = () => {
    window.print();
  };

  const handleModal = (row, entity) => {
    console.log(row, entity);
    setSelectedRow({ row: row, entity: entity });
    setOpenModal(true);
  };
  return !loading ? (
    <div className="grid h-screen w-screen grid-cols-4 grid-rows-3 gap-0 bg-gray-200">
      <div className=" flex items-center justify-center overflow-auto bg-white ">
        <DataTable
          data={ipsList}
          columns={aosList ? [...aosList].reverse() : []}
          rowKey="id"
          cellKey="id"
          cellContent={(row, cell) =>
            row["aoId"] === cell["id"] ? (
              // <div className="h-full w-full border-2 border-orange-600 bg-orange-200 ">
              <CircleIndicator />
            ) : (
              // </div>
              "\u00A0"
            )
          }
        />
      </div>

      <div className=" flex items-center justify-center overflow-auto bg-white">
        <DataTable2
          data={ipsList}
          columns={[{}]}
          rowKey="id"
          cellKey="id"
          cellContent={(row) => (
            <>
              <div className="flex flex-row justify-between">
                <span>{row["label"]} &nbsp;</span>
                <span
                  className="flex flex-row items-center gap-2 hover:cursor-pointer"
                  onClick={() => handleEdit(row, "ips")}
                >
                  <span>{formatToTwoDecimalPlaces(row["advancement"])}%</span>{" "}
                  <Trend data={getCharFromValue(row["trend"])} />
                  <MdCircle className={getTailwindColor(row["advancement"])} />
                  <MdModeEditOutline />
                </span>
              </div>
            </>
          )}
        />
      </div>

      <div className=" flex items-center justify-center overflow-auto bg-white">
        <DataTable
          data={ipsList ? ipsList : []}
          columns={itsList ? itsList : []}
          rowKey="id"
          cellKey="id"
          cellContent={(row, cell) => {
            return cell && cell["ipId"] === row["id"] ? (
              // <div className="h-full w-full border-2 border-orange-600 bg-orange-200 ">
              <CircleIndicator />
            ) : (
              // </div>
              "\u00A0"
            );
          }}
        />
      </div>
      <div className=" flex h-full items-center justify-center overflow-auto bg-white">
        <div className="w-1/7 flex h-full items-center justify-center ">
          <span className="rotate-180" style={{ writingMode: "vertical-rl" }}>
            &nbsp;
          </span>
        </div>{" "}
        <DataTable
          data={ipsList || []}
          columns={filteredResources.map((resource) => ({
            key: resource["id"],
            title: resource["department"],
          }))}
          rowKey="Id-II"
          cellKey="Id-HR"
          cellContent={(row, col) => {
            if (!col) return "\u00A0";
            console.log("filteredResources", filteredResources);
            const relatedIT = itsList.filter(
              (it) => it["resourceId"] === col.key,
            );
            console.log("relatedIT", relatedIT);

            const relatedII = relatedIT.map((it) =>
              itsList.find((ii) => ii["id"] === it["id"]),
            );
            console.log("relatedII", relatedII);

            //
            // console.log(row["Id-II"]);
            const isRelated = relatedII.some((ii) => ii["ipId"] === row["id"]);
            console.log("isRelated", isRelated);

            return isRelated ? (
              // <div className="h-full w-full border-2 border-orange-600 bg-orange-200 ">
              <CircleIndicator />
            ) : (
              // </div>
              "\u00A0"
            );
          }}
        />
      </div>

      <div className=" flex rotate-180 items-center justify-center overflow-auto bg-white">
        <DataTableRotation
          data={aosList ? [...aosList] : []}
          rowKey="id"
          cellKey="id"
          cellContent={(row) => (
            <div className="flex items-center justify-between overflow-hidden whitespace-normal text-xs ">
              <>
                {row["label"]}
                <span
                  className="flex flex-row items-center gap-2 hover:cursor-pointer"
                  onClick={() => handleEdit(row, "aos")}
                >
                  <span>{formatToTwoDecimalPlaces(row["advancement"])}%</span>{" "}
                  <Trend data={getCharFromValue(row["trend"])} />

                  <MdCircle className={getTailwindColor(row["advancement"])} />
                  <MdModeEditOutline />
                </span>
                {/* </div> */}
              </>
            </div>
          )}
          style={{ writingMode: "vertical-rl" }}
        />
      </div>

      <div className=" flex items-center justify-center overflow-auto bg-white">
        <GeometricSections refreshMatrix={fetchMatrix} />
      </div>

      <div className=" flex items-center justify-center overflow-auto bg-white">
        <DataTableRotation
          data={itsList ? [...itsList] : []}
          rowKey="id"
          cellKey="id"
          cellContent={(row) => (
            <div className="flex h-full w-full rotate-180 items-center justify-between overflow-hidden whitespace-normal text-xs">
              <span> {row["label"]}</span>
              <span className="flex items-center justify-center gap-2">
                {formatToTwoDecimalPlaces(row["advancement"])}%
                <Trend data={getCharFromValue(row["trend"])} />

                <MdCircle className={getTailwindColor(row["advancement"])} />
                <span
                  className="hover:cursor-pointer"
                  onClick={() => handleEdit(row, "its")}
                >
                  <MdModeEditOutline />
                </span>
              </span>
            </div>
          )}
          style={{ writingMode: "vertical-rl" }}
        />
      </div>

      <div className=" flex items-center justify-center overflow-auto bg-white ">
        <div className="w-1/7 flex h-full items-center justify-center  ">
          <span
            className="flex h-full w-full rotate-180 items-center justify-between"
            style={{ writingMode: "vertical-rl" }}
          >
            Resource{" "}
            <span
              className="hover:cursor-pointer"
              onClick={() => handleAdd("hrs")}
            >
              <IoAddCircleOutline />
            </span>
          </span>
        </div>

        <DataTableRotation
          data={
            resources
              ? resources.filter((hr) =>
                  itsList.some((it) => it["resourceId"] === hr["id"]),
                )
              : []
          }
          // data={resources}
          // rowKey="Id-HR"
          // cellKey="Id-HR"
          rowKey="id"
          cellKey="id"
          cellContent={(row) => (
            <div className="flex rotate-180 items-center justify-between  overflow-hidden whitespace-normal text-xs">
              {/* {row["Department"]} */}
              {/* {row["Department"]} */}
             <span className="">{row["department"]}</span>


              <span
                className="hover:cursor-pointer flex flex-row items-center gap-2"
                onClick={() => handleEdit(row, "hrs")}
              >
                <Trend data={getCharFromValue(row["trend"])} />
                <MdModeEditOutline />

              </span>
            </div>
          )}
          style={{ writingMode: "vertical-rl" }}
        />
      </div>

      <div className=" flex items-center justify-center overflow-auto bg-white">
        <DataTable
          data={sosList}
          columns={aosList ? [...aosList].reverse() : []}
          rowKey="id"
          cellKey="id"
          cellContent={(row, cell) =>
            cell["soId"] === row["id"] ? (
              // <div className="h-full w-full border-2 border-orange-600 bg-orange-200 ">
              <CircleIndicator />
            ) : (
              // </div>
              "\u00A0"
            )
          }
        />
      </div>

      <div className=" flex items-center justify-center overflow-auto bg-white">
        <DataTable2
          data={sosList}
          columns={[{}]}
          rowKey="id"
          cellKey="id"
          cellContent={(row) => (
            <>
              <div className="flex flex-row justify-between">
                {row["label"]}
                <span
                  className="flex flex-row items-center gap-2 hover:cursor-pointer"
                  onClick={() => handleEdit(row, "sos")}
                >
                  <span>{formatToTwoDecimalPlaces(row["advancement"])}%</span>{" "}
                  <Trend data={getCharFromValue(row["trend"])} />

                  <MdCircle className={getTailwindColor(row["advancement"])} />
                  <MdModeEditOutline />
                </span>
              </div>
            </>
          )}
        />
      </div>

      <div className=" flex items-center justify-center overflow-auto bg-white p-4 text-center">
        <div className="flex flex-col gap-3">
          <p className="text-2xl font-bold">
            {/* <img
              src="/images/logo.jpeg"
              alt=""
              style={{ height: "100px", width: "200px" }}
            /> */}
            <Logo />
          </p>
          <IconWrapper IconComponent={MdLocalPrintshop} onClick={handlePrint} />{" "}
          {/* <IconWrapper IconComponent={MdEditSquare} /> */}
          <IconWrapper
            IconComponent={IoArrowBack}
            // label="Back to the X-Matrix"
            onClick={() =>
              // navigate to the xmatrix show
              navigate(`/matrices/show/${id}`)
            }
          />
        </div>
      </div>
      <div className=" flex items-center justify-center overflow-auto bg-white p-4 text-center">
        <p className="text-sm">
          {/* {matrix?.id}
          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Earum
          eligendi recusandae iusto? Cupiditate natus illum at sed, dolore ipsa
          laboriosam similique aperiam placeat officiis accusamus blanditiis
          accusantium aliquid, quod minima? */}
        </p>
      </div>

      {openModal == "sos" ? (
        <StrategicObjectiveModal
          isOpen={true}
          onClose={() => setOpenModal(false)}
          onSubmit={selectedData ? updateSos : "addObjective"}
          initialData={selectedData}
        />
      ) : openModal == "aos" ? (
        <AnnualObjectiveModal
          isOpen={true}
          onClose={() => setOpenModal(false)}
          onSubmit={selectedData ? updateAos : "addObjective"}
          initialData={selectedData}
        />
      ) : openModal == "its" ? (
        <ImplementationTacticsModal
          isOpen={true}
          onClose={() => setOpenModal(false)}
          onSubmit={selectedData ? updateIts : "addObjective"}
          initialData={selectedData}
        />
      ) : openModal == "ips" ? (
        <ImprovementPrioritiesModal
          isOpen={true}
          onClose={() => setOpenModal(false)}
          onSubmit={selectedData ? updateIps : "addObjective"}
          initialData={selectedData}
        />
      ) : openModal == "hrs" ? (
        <ResourceModal
          isOpen={true}
          onClose={() => setOpenModal(false)}
          onSubmit={selectedData ? updateHrs : addHrs}
          initialData={selectedData}
        />
      ) : (
        <></>
      )}
    </div>
  ) : (
    <div className="flex h-screen w-screen items-center justify-center bg-gray-200">
      <h3>Loading...</h3>
    </div>
  );
};

export default GeneratedMatrix;
