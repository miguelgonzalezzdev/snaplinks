import { Navigate, Outlet } from "react-router"
import { useAuthStore } from "../store/useAuthStore"
import { useEffect, useState } from "react"
import { Loader } from "../components/Loader"

export const ProtectedRoute = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  const loadSession = useAuthStore((state) => state.loadSession)
  const refreshTokenFn = useAuthStore((state) => state.refreshTokenFn)
  const [loading, setLoading] = useState(true)
  const [authorized, setAuthorized] = useState(false)

  useEffect(() => {
    const checkAuth = async () => {

      // Si ya está autenticado
      if (isAuthenticated) {
        setAuthorized(true)
        setLoading(false)
        return
      }

      // Intentamos cargar sesion previa
      const session = loadSession()
      if (session.success) {

        // Refrescamos token
        const refreshResult = await refreshTokenFn()
        setAuthorized(refreshResult.success)
        setLoading(false)
        return
      }
      
      // Si todo falla, no autorizado
      setAuthorized(false)
      setLoading(false)
    }

    checkAuth()
  }, [isAuthenticated, loadSession, refreshTokenFn])

  if (loading) return <Loader />

  return authorized ? <Outlet /> : <Navigate to="/login" replace />
}
