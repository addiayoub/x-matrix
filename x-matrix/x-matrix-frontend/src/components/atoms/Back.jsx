import React from 'react';
import { Link } from 'react-router-dom';
import { IoMdArrowRoundBack } from 'react-icons/io';

const Back = ({ path }) => {
  return (
    <div className='flex flex-row gap-2 items-center text-sm'>
      <Link to={path} className='flex flex-row gap-2 items-center text-blue-600 dark:text-blue-500 hover:underline'>
        <IoMdArrowRoundBack />
        Back
      </Link>
    </div>
  );
};

export default Back;