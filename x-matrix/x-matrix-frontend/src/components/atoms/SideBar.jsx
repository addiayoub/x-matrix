import { Avatar, Dropdown } from "flowbite-react";
import React from "react";
import { Link } from "react-router-dom";
import { logout } from "../../features/auth/AuthSlice";
import { useDispatch, useSelector } from "react-redux";
import { use } from "react";
import Logo from "./Logo";
import { FaChessBoard, FaHome, FaRegBuilding, FaUsers } from "react-icons/fa";
import { PiMatrixLogo } from "react-icons/pi";

const SideBar = () => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.auth?.user);
  const handleSignOut = () => {
    try {
      dispatch(logout());
      console.log("Signed out successfully");
    } catch (error) {
      console.error("Error signing out:", error);
    }
  };
  return (
    <>
      <nav class="fixed top-0 z-50 w-full border-b border-gray-200 bg-white dark:border-gray-700 dark:bg-gray-800">
        <div class="px-3 py-3 lg:px-5 lg:pl-3">
          <div class="flex items-center justify-between">
            <div class="flex items-center justify-start rtl:justify-end">
              <button
                data-drawer-target="logo-sidebar"
                data-drawer-toggle="logo-sidebar"
                aria-controls="logo-sidebar"
                type="button"
                class="inline-flex items-center rounded-lg p-2 text-sm text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600 sm:hidden"
              >
                <span class="sr-only">Open sidebar</span>
                <svg
                  class="h-6 w-6"
                  aria-hidden="true"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    clip-rule="evenodd"
                    fill-rule="evenodd"
                    d="M2 4.75A.75.75 0 012.75 4h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 4.75zm0 10.5a.75.75 0 01.75-.75h7.5a.75.75 0 010 1.5h-7.5a.75.75 0 01-.75-.75zM2 10a.75.75 0 01.75-.75h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 10z"
                  ></path>
                </svg>
              </button>
              <a class="ms-2 flex md:me-24">
                <span class="self-center whitespace-nowrap text-xl font-semibold dark:text-white sm:text-2xl">
                  <Logo styles={{ height: "50px", width: "160px" }} />
                  {/* <img src="/images/logo.jpeg" alt=""  style={{ height: '50px'  , width: '160px'}} /> */}
                </span>
              </a>
            </div>
            <div class="flex items-center">
              <div class="ms-3 flex items-center">
                <Dropdown
                  label={<Avatar placeholderInitials="RR" rounded />}
                  arrowIcon={false}
                  inline
                >
                  <Dropdown.Header>
                    <span className="block text-sm">
                      {user && user?.username}
                    </span>
                    <span className="block truncate text-sm font-medium">
                      {user && user?.sub}
                    </span>
                  </Dropdown.Header>
                  <Dropdown.Item>
                    <Link to="/dashboard">Dashboard</Link>
                  </Dropdown.Item>
                  <Dropdown.Item>Settings</Dropdown.Item>
                  <Dropdown.Divider />
                  <Dropdown.Item onClick={handleSignOut}>
                    Sign out
                  </Dropdown.Item>
                </Dropdown>
              </div>
            </div>
          </div>
        </div>
      </nav>

      <aside
        id="logo-sidebar"
        class="fixed left-0 top-0 z-40 h-screen w-64 -translate-x-full border-r border-gray-200 bg-white pt-20 transition-transform dark:border-gray-700 dark:bg-gray-800 sm:translate-x-0"
        aria-label="Sidebar"
      >
        <div class="h-full overflow-y-auto bg-white px-3 pb-4 dark:bg-gray-800">
          <ul class="space-y-2 font-medium">
            <li>
              <a class="group flex items-center rounded-lg p-2 text-gray-900 hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700">
                <FaHome />

                <Link to="/dashboard" class="ms-3">
                  Dashboard
                </Link>
                {/* <span class="ms-3">Dashboard</span> */}
              </a>
            </li>
            <li>
              <a class="group flex items-center rounded-lg p-2 text-gray-900 hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700">
                <FaChessBoard />

                <Link to="/matrices" class="ms-3">
                  Matrices
                </Link>
              </a>
            </li>

            {user?.role === "SolutionOwner" && (
              <>
                <li>
                  <a class="group flex items-center rounded-lg p-2 text-gray-900 hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700">
                    <FaRegBuilding />
                    <Link to="/companies" class="ms-3">
                      Companies
                    </Link>
                  </a>
                </li>
                
              </>
            )}

            {(user?.role === "GlobalOwner" ||
              user?.role === "SolutionOwner" ||
              user?.role === "DomainOwner") && (
                <>
                 {/* <li>
                <a class="group flex items-center rounded-lg p-2 text-gray-900 hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700">
                <FaChessBoard />                  <Link to="/matrix-hierarchy" class="ms-3">
                    User Matrices
                  </Link>
                </a>
              </li> */}
              <li>
                <a class="group flex items-center rounded-lg p-2 text-gray-900 hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700">
                  <FaUsers />
                  <Link to="/users" class="ms-3">
                    Users
                  </Link>
                </a>
              </li></>
            )}
          </ul>
        </div>
      </aside>
    </>
  );
};

export default SideBar;
