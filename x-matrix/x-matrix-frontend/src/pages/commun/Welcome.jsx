import React from 'react'
import Logo from '../../components/atoms/Logo'

const Welcome = () => {
    const token = localStorage.getItem('token'); 
    
  return (
    
    <div className="flex flex-col items-center justify-center h-screen bg-white dark:bg-gray-900">
        <h1 className="text-4xl font-bold mb-4">Welcome to the Application!</h1>
        <p className="text-lg text-gray-700 mb-8">
            <span>Please continue </span>
            {/* link to login */}


            {!token ? (
                <>
                <span>to login</span>
              <a href="/login" className="text-blue-500 hover:underline">Login</a>
            </>)
            : (
                <>
                <span>to the </span>
              <a href="/dashboard" className="text-blue-500 hover:underline">Dashboard</a>
            </>)}
        </p>

        
        <Logo styles={{ height: "150px", width: "300px" }} />
        <p className="text-sm text-gray-500">© 2023 Bridge Strategy Solutions. All rights reserved.</p>
        <p className="text-sm text-gray-500">Version 1.0.0</p>
    </div>
  )
}

export default Welcome