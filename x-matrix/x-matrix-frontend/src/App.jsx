import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Dashboard from "./pages/commun/Dashboard";
import Matrices from "./pages/Matrix/Matrices";
import AppLayout from "./layouts/AppLayout";
import ShowMatrix from "./pages/Matrix/ShowMatrix";
import Sos from "./pages/So/Sos";
import Login from "./pages/commun/Login";
import Auth from "./middlewares/Auth";
import Aos from "./pages/Ao/Aos";
import Ips from "./pages/Ip/Ips";
import Its from "./pages/It/Its";
import GeneratedMatrix from "./pages/GeneratedMatrix/GeneratedMatrix";
import Hrs from "./pages/Hr/Hrs";
import Welcome from "./pages/commun/Welcome";
import HierarchicalOrganizationChart from "./components/molecules/HierarchicalOrganizationChart";
import Companies from "./pages/Company/Companies";
import Users from "./pages/User/Users";
import Unauthorized from "./pages/commun/Unauthorized";
import MatrixHierarchy from "./pages/Matrix/MatrixHierarchy";
import { useEffect } from "react";
import { jwtDecode } from "jwt-decode";
import { useDispatch } from "react-redux";
import { setRole, setUser } from "./features/auth/AuthSlice";

function App() {
  const dispatch = useDispatch();
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        dispatch(setUser(decoded));  
        dispatch(setRole(decoded.role));  
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }
  }, [dispatch]);

  const router = createBrowserRouter([
    {
      path: "/",
      element: <Welcome />,
    },
    {
      path: "/unauthorized",
      element: <Unauthorized />,
    },
    {
      path: "/login",
      element: <Login />,
    },

    {
      path: "/chart",
      element: <Auth component={AppLayout} requiredRoles={["SolutionOwner"]} />,
      children: [
        {
          path: "",
          element: <HierarchicalOrganizationChart />,
        },
      ],
    },
    {
      path: "matrix-hierarchy",
      element: <Auth component={AppLayout} requiredRoles={['SolutionOwner' ,'DomainOwner','GlobalOwner']} />,
      children: [
        {
          path: "",
          element: <MatrixHierarchy />,
        },
      ],
    },
    {
      path: "/",
      element: (
        <Auth
          component={AppLayout}
          //  requiredRoles={['SolutionOwner' , 'MatrixOwner','DomainOwner','GlobalOwner']}
        />
      ),
      children: [
        {
          path: "/dashboard",
          element: <Dashboard />,
        },
        {
          path: "/matrices",
          children: [
            {
              path: "",
              element: <Matrices />,
            },
            {
              path: "show/:id",
              element: <ShowMatrix />,
            },
          ],
        },
        {
          path: "companies",
          element: <Companies />,
        },
        {
          path: "users",
          element: <Users />,
        },
        {
          path: "strategic-objectives/:id",
          element: <Sos />,
        },
        {
          path: "annual-objectives/:id",
          element: <Aos />,
        },
        {
          path: "improvement-priorities/:id",
          element: <Ips />,
        },
        {
          path: "implementation-tasks/:id",
          element: <Its />,
        },
        {
          path: "resources/:id",
          element: <Hrs />,
        },
      ],
    },
    {
      path: "generated-matrix/:id",
      element: <Auth component={GeneratedMatrix} />,
    },
  ]);

  return <RouterProvider router={router} />;
}

export default App;
