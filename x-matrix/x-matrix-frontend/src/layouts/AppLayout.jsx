import React from 'react'
import SideBar from '../components/atoms/SideBar'
import { Outlet } from 'react-router-dom'

const AppLayout = () => {
  
  return (
    <>
    <SideBar />

<div class="p-4 sm:ml-64">
   <div class="p-4 mt-14 overflow-auto ">
     

      <Outlet />
   </div>
</div>
    </>
  )
}

export default AppLayout