import React from 'react';
import { useSelector } from 'react-redux';
import HierarchicalOrganizationChart from '../../components/molecules/HierarchicalOrganizationChart';

const Dashboard = () => {
  const user = useSelector((state) => state.auth?.user);
  
  return (
    <div className="w-full h-full overflow-hidden">
      <div className="p-4">
        <p className="text-md mb-4">
          Welcome <span className="font-medium">{user?.username}</span> to the Dashboard, your role is <span className="font-medium">{user?.role}</span>
        </p>
      </div>
      
      {user?.role === "SolutionOwner" && (
        <>
        <a href="/chart">
          <button className="bg-blue-500 text-white px-4 py-2 rounded-md mb-4">Go to Chart</button>
        </a>
        <div className="w-full overflow-x-auto overflow-y-auto" >
          <div className="min-w-max p-4">
            <HierarchicalOrganizationChart />
          </div>
        </div>
        </>
      )}
      
    </div>
  );
};

export default Dashboard;