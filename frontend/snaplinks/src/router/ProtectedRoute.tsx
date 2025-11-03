import { Navigate, Outlet } from "react-router"
import { useAuthStore } from "../store/useAuthStore"
import { useEffect, useState } from "react"
import { Loader } from "../components/Loader"

export const ProtectedRoute = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  const loadSession = useAuthStore((state) => state.loadSession)
  const refreshTokenFn = useAuthStore((state) => state.refreshTokenFn)
  const expiresAt = useAuthStore((state) => state.expiresAt)
  const [authorized, setAuthorized] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const checkAuth = async () => {

      const now = Date.now()

      // Si ya está autenticado y token no expirado
      if (isAuthenticated && expiresAt && now < expiresAt) {
        setAuthorized(true)
        setLoading(false)
        return
      }

      // Intentamos cargar sesion previa
      const session = loadSession()
      if (session.success) {

        // Si la sesion es valida
        if (session.isValid) {
          setAuthorized(true)
          setLoading(false)
          return
        }

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
  }, [isAuthenticated, loadSession, refreshTokenFn, expiresAt])

  if (loading) return <Loader />

  return authorized ? <Outlet /> : <Navigate to="/login" replace />
}
