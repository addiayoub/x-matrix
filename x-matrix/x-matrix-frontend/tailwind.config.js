import flowbite from "flowbite-react/tailwind";

/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}", flowbite.content()],
  theme: {
    extend: {
      colors: {
        primary: {"50":"#eff6ff","100":"#dbeafe","200":"#bfdbfe","300":"#93c5fd","400":"#60a5fa","500":"#3b82f6","600":"#2563eb","700":"#1d4ed8","800":"#1e40af","900":"#1e3a8a","950":"#172554"},
        'web-orange': {
    '50': '#fffcea',
    '100': '#fff5c5',
    '200': '#ffeb85',
    '300': '#ffda46',
    '400': '#ffc71b',
    '500': '#ffa500',
    '600': '#e27c00',
    '700': '#bb5502',
    '800': '#984208',
    '900': '#7c360b',
    '950': '#481a00',
},
'tangerine': {
    '50': '#fffbec',
    '100': '#fff5d3',
    '200': '#ffe8a5',
    '300': '#ffd66d',
    '400': '#ffb832',
    '500': '#ffa00a',
    '600': '#ff8800',
    '700': '#cc6402',
    '800': '#a14d0b',
    '900': '#82410c',
    '950': '#461f04',
},



      }
    },
  },
  plugins: [flowbite.plugin()],
};
