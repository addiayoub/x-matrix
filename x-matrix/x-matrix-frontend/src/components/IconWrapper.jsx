import React from "react";

const IconWrapper = ({ IconComponent, size = 30, className = "text-orange-500" , onClick , label}) => {
  return (
  <div  onClick={onClick}  className={`cursor-pointer ${className} flex flex-row gap-3 items-center `}>
    <IconComponent
      size={size}
     
     

    />
    {label && <span className="text-sm">{label}</span>}
  </div>)
};

export default IconWrapper;