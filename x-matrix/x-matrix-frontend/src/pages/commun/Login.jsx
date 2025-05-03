import React from "react";
import { login } from "../../features/auth/AuthThunk";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import Logo from "../../components/atoms/Logo";

const Login = () => {
  const dispatch = useDispatch();

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData);
    console.log(data);

    dispatch(login(data)).then((res) => {
      if (res.meta.requestStatus === "fulfilled") {
        console.log("Login successful");

        console.log(res.payload.data);
        navigate("/dashboard"); 
      } else {
        alert("Login failed");
        console.log("Login failed");
      }
    });
  };

  return (
    <section class=" ">
      <div class="mx-auto flex flex-col items-center justify-center px-6 py-8 md:h-screen lg:py-0">
        
        <Logo styles={{ height: "100px",  }} />
        <div class="w-full rounded-lg bg-white shadow dark:border dark:border-gray-700 dark:bg-gray-800 sm:max-w-md md:mt-0 xl:p-0">
          <div class="space-y-4 p-6 sm:p-8 md:space-y-6">
           
            <form class="space-y-4 md:space-y-6" onSubmit={handleSubmit}>
              <div>
                <label
                  for="email"
                  class="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
                >
                  Your email
                </label>
                <input
                  type="text"
                  name="email"
                  id="email"
                  class="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-gray-900 focus:border-primary-600 focus:ring-primary-600 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                  placeholder="name@company.com"
                />
              </div>
              <div>
                <label
                  for="password"
                  class="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
                >
                  Password
                </label>
                <input
                  type="password"
                  name="password"
                  id="password"
                  placeholder="••••••••"
                  class="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-gray-900 focus:border-primary-600 focus:ring-primary-600 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                />
              </div>

              <button
                type="submit"
                class="w-full rounded-lg bg-orange-500 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-orange-400 focus:outline-none focus:ring-4 focus:ring-primary-300 dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
              >
                Login
              </button>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Login;
