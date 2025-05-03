import { useState, useEffect, useRef } from 'react';

const enhancedData = [
  {
    "id": 2,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Solution Owner (Super Admin)",
    "user": {
      "id": 2,
      "firstName": null,
      "lastName": null,
      "email": "superadmin@gmail.com",
      "username": "superadmin@gmail.com",
      "roleName": "SolutionOwner",
      "parentId": null,
      "company": {
        "id": 1,
        "name": "Global",
        "location": "Global",
        "logo": "Logo1.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 3,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Global-Owner-A02",
    "user": {
      "id": 3,
      "firstName": null,
      "lastName": null,
      "email": "CEOINWI@gmail.com",
      "username": "CEOINWI@gmail.com",
      "roleName": "GlobalOwner",
      "parentId": 2,
      "company": {
        "id": 2,
        "name": "INWI",
        "location": "Casablanca",
        "logo": "Logo2.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 4,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Global-Owner-A01",
    "user": {
      "id": 4,
      "firstName": null,
      "lastName": null,
      "email": "CEOCDG@gmail.com",
      "username": "CEOCDG@gmail.com",
      "roleName": "GlobalOwner",
      "parentId": 2,
      "company": {
        "id": 3,
        "name": "CDG",
        "location": "Rabat",
        "logo": "Logo3.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 5,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Domain-Owner-A01-B02",
    "user": {
      "id": 5,
      "firstName": null,
      "lastName": null,
      "email": "VPFinance@gmail.com",
      "username": "VPFinance@gmail.com",
      "roleName": "DomainOwner",
      "parentId": 4,
      "company": {
        "id": 4,
        "name": "Finance",
        "location": "Global",
        "logo": "Logo4.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 6,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Domain-Owner-A01-B01",
    "user": {
      "id": 6,
      "firstName": null,
      "lastName": null,
      "email": "HR@gmail.com",
      "username": "HR@gmail.com",
      "roleName": "DomainOwner",
      "parentId": 4,
      "company": {
        "id": 5,
        "name": "Human Resources",
        "location": "Global",
        "logo": "Logo5.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 7,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Matrix-Owner-A01-B02-C02",
    "user": {
      "id": 7,
      "firstName": null,
      "lastName": null,
      "email": "DRAccounting@gmail.com",
      "username": "DRAccounting@gmail.com",
      "roleName": "MatrixOwner",
      "parentId": 5,
      "company": {
        "id": 6,
        "name": "Accounting",
        "location": "Global",
        "logo": "Logo6.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 8,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Matrix-Owner-A01-B02-C01",
    "user": {
      "id": 8,
      "firstName": null,
      "lastName": null,
      "email": "DRProcurement@gmail.com",
      "username": "DRProcurement@gmail.com",
      "roleName": "MatrixOwner",
      "parentId": 5,
      "company": {
        "id": 7,
        "name": "Procurement",
        "location": "Global",
        "logo": "Logo7.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 9,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Matrix-Owner-A01-B01-C01",
    "user": {
      "id": 9,
      "firstName": null,
      "lastName": null,
      "email": "PayrollManager@gmail.com",
      "username": "PayrollManager@gmail.com",
      "roleName": "MatrixOwner",
      "parentId": 6,
      "company": {
        "id": 8,
        "name": "Payroll",
        "location": "Global",
        "logo": "Logo8.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 10,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Domain-Owner-A02-B01",
    "user": {
      "id": 10,
      "firstName": null,
      "lastName": null,
      "email": "VP-B2C@gmail.com",
      "username": "VP-B2C@gmail.com",
      "roleName": "DomainOwner",
      "parentId": 3,
      "company": {
        "id": 9,
        "name": "B2C",
        "location": "Global",
        "logo": "Logo9.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 11,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Matrix-Owner-A02-B01-C01",
    "user": {
      "id": 11,
      "firstName": null,
      "lastName": null,
      "email": "DirectorMarketing@gmail.com",
      "username": "DirectorMarketing@gmail.com",
      "roleName": "MatrixOwner",
      "parentId": 10,
      "company": {
        "id": 10,
        "name": "Marketing",
        "location": "Global",
        "logo": "Logo10.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  },
  {
    "id": 12,
    "deleted": false,
    "createdAt": "2025-04-27T16:05:30.000+00:00",
    "updatedAt": "2025-04-27T16:05:30.000+00:00",
    "label": "Matrix-Owner-A02-B01-C02",
    "user": {
      "id": 12,
      "firstName": null,
      "lastName": null,
      "email": "DirectorSales@gmail.com",
      "username": "DirectorSales@gmail.com",
      "roleName": "MatrixOwner",
      "parentId": 10,
      "company": {
        "id": 11,
        "name": "Sales",
        "location": "Global",
        "logo": "Logo11.png",
        "createdAt": "2025-04-25T18:32:37"
      }
    }
  }
];

const buildHierarchy = (data) => {
  const items = {};
  
  data.forEach(item => {
    items[item.id] = { ...item, children: [] };
  });
  
  data.forEach(item => {
    if (item.user.parentId && items[item.user.parentId]) {
      items[item.user.parentId].children.push(items[item.id]);
    }
  });
  
  return items[2];
};

const NodeBox = ({ data, id }) => {
  return (
    <div 
      id={`node-${id}`}
      className={`rounded-md border ${data.boxClass} p-4 min-w-64 max-w-64 text-sm shadow-sm`}
    >
      <div className="font-semibold text-gray-700">{data.label}</div>
      {data.user && data.user.company && (
        <div key="company" className="flex justify-between mt-1">
          <span className="text-gray-500">Company</span>
          <span className="text-gray-800 font-medium">{data.user.company.name}</span>
        </div>
      )}
    </div>
  );
};

const HierarchicalOrganizationChart = () => {
  const [hierarchy, setHierarchy] = useState(null);
  const [connections, setConnections] = useState([]);
  const [dimensions, setDimensions] = useState({ width: 1000, height: 800 });
  const chartRef = useRef(null);
  const nodeRefs = useRef({});
  
  useEffect(() => {
    const hierarchyData = buildHierarchy(enhancedData);
    setHierarchy(hierarchyData);
    
    // Add resize listener
    const handleResize = () => {
      if (chartRef.current) {
        calculateConnections(hierarchy);
      }
    };
    
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  useEffect(() => {
    if (hierarchy) {
      setTimeout(() => {
        calculateConnections(hierarchy);
      }, 300);
    }
  }, [hierarchy]);

  const calculateConnections = (node) => {
    if (!node || !chartRef.current) return;
    
    const newConnections = [];
    let minX = Infinity;
    let maxX = 0;
    let minY = Infinity;
    let maxY = 0;
    
    const traverseNode = (node, parentId) => {
      const nodeElement = document.getElementById(`node-${node.id}`);
      
      if (!nodeElement) return;
      
      const nodeRect = nodeElement.getBoundingClientRect();
      const chartRect = chartRef.current.getBoundingClientRect();
      
      const nodeX = nodeRect.left - chartRect.left + (nodeRect.width / 2);
      const nodeY = nodeRect.top - chartRect.top + (nodeRect.height / 2);
      
      minX = Math.min(minX, nodeX - nodeRect.width / 2);
      maxX = Math.max(maxX, nodeX + nodeRect.width / 2);
      minY = Math.min(minY, nodeY - nodeRect.height / 2);
      maxY = Math.max(maxY, nodeY + nodeRect.height / 2);
      
      nodeRefs.current[node.id] = { x: nodeX, y: nodeY, width: nodeRect.width, height: nodeRect.height };
      
      if (parentId) {
        const parentPos = nodeRefs.current[parentId];
        
        if (parentPos) {
          newConnections.push({
            id: `${parentId}-${node.id}`,
            x1: parentPos.x,
            y1: parentPos.y + (parentPos.height / 2),
            x2: nodeX,
            y2: nodeY - (nodeRect.height / 2),
            parentId,
            childId: node.id
          });
        }
      }
      
      if (node.children && node.children.length > 0) {
        node.children.forEach(child => {
          traverseNode(child, node.id);
        });
      }
    };
    
    traverseNode(node, null);
    
    setDimensions({
      width: Math.max(1000, maxX - minX + 100),
      height: Math.max(800, maxY - minY + 100)
    });
    
    setConnections(newConnections);
  };
  
  const renderNode = (node) => {
    return (
      <div key={node.id} className="flex flex-col items-center mb-12">
        <NodeBox data={node} id={node.id} />
        
        {node.children && node.children.length > 0 && (
          <div className="flex space-x-16 mt-16">
            {node.children.map(child => renderNode(child))}
          </div>
        )}
      </div>
    );
  };
  
  if (!hierarchy) return <div className="p-4">Loading...</div>;
  
  return (
    <div className="p-4">
      <div className="flex justify-center mb-8">
        <h1 className="text-xl font-bold text-gray-700">Organizational Hierarchy</h1>
      </div>
      
      <div className="relative" ref={chartRef} style={{ minWidth: dimensions.width, minHeight: dimensions.height }}>
        <svg 
          className="absolute top-0 left-0 pointer-events-none" 
          style={{ zIndex: 10 }}
          width={dimensions.width} 
          height={dimensions.height}
        >
          {connections.map(conn => {
            const midY = conn.y1 + (conn.y2 - conn.y1) / 2;
            return (
              <g key={conn.id}>
                <path
                  d={`M ${conn.x1},${conn.y1} 
                     L ${conn.x1},${midY} 
                     Q ${conn.x1},${midY+10} ${conn.x1+10},${midY+10} 
                     L ${conn.x2-10},${midY+10} 
                     Q ${conn.x2},${midY+10} ${conn.x2},${midY+20} 
                     L ${conn.x2},${conn.y2}`}
                  fill="none"
                  stroke="#9CA3AF"
                  strokeWidth="2"
                />
                <circle cx={conn.x1} cy={conn.y1} r="3" fill="#6B7280" />
                <circle cx={conn.x2} cy={conn.y2} r="3" fill="#6B7280" />
              </g>
            );
          })}
        </svg>
        
        <div className="flex justify-center relative z-20 mb-16">
          {renderNode(hierarchy)}
        </div>
      </div>
    </div>
  );
};

export default HierarchicalOrganizationChart;
