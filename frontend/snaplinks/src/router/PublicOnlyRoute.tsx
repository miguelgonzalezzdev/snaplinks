import { Navigate, Outlet } from "react-router"
import { useAuthStore } from "../store/useAuthStore"

export const PublicOnlyRoute = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)

  return isAuthenticated ? <Navigate to="/" replace /> : <Outlet />
}
