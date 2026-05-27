import { createContext, useEffect, useMemo, useState } from "react";
import { getProfile, loginUser, registerUser } from "../api/authApi";

export const AuthContext = createContext(null);

const normaliseUser = (data) => {
  if (!data) return null;
  return {
    ...data,
    userId: data.userId ?? data.id,
  };
};

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [authLoading, setAuthLoading] = useState(true);

  const roleName = (user?.roleName || "").toLowerCase();
  const isAdmin = ["admin", "role_admin", "administrator", "super_admin", "super-admin"].includes(roleName);
  const isProducer = ["producer", "role_producer"].includes(roleName);

  const loadProfile = async () => {
    const token = localStorage.getItem("token");

    if (!token) {
      setUser(null);
      setAuthLoading(false);
      return null;
    }

    try {
      const response = await getProfile();
      const profile = normaliseUser(response.data);
      setUser(profile);
      localStorage.setItem("authUser", JSON.stringify(profile));
      return profile;
    } catch {
      localStorage.removeItem("token");
      localStorage.removeItem("userId");
      localStorage.removeItem("authUser");
      setUser(null);
      return null;
    } finally {
      setAuthLoading(false);
    }
  };

  const login = async (formData) => {
    const response = await loginUser({
      usernameOrEmail: formData.usernameOrEmail,
      password: formData.password,
    });

    localStorage.setItem("token", response.data.token);
    localStorage.setItem("userId", response.data.userId);

    const profile = await loadProfile();
    return profile || normaliseUser(response.data);
  };

  const register = async (formData) => registerUser(formData);

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("authUser");
    setUser(null);
    window.location.href = "/login";
  };

  useEffect(() => {
    const cachedUser = localStorage.getItem("authUser");
    if (cachedUser && localStorage.getItem("token")) {
      try {
        setUser(JSON.parse(cachedUser));
      } catch {
        localStorage.removeItem("authUser");
      }
    }

    loadProfile();
  }, []);

  const value = useMemo(
    () => ({
      user,
      setUser,
      isAdmin,
      isProducer,
      authLoading,
      login,
      register,
      logout,
      loadProfile,
    }),
    [user, isAdmin, isProducer, authLoading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export default AuthProvider;
