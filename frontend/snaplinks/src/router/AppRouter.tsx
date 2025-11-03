import { Navigate, Route, Routes } from "react-router"
import LandingPage from "../components/LandingPage"
import DashboardPage from "../components/DashboardPage"
import LoginPage from "../components/LoginPage"
import RegisterPage from "../components/RegisterPage"
import { PublicOnlyRoute } from "./PublicOnlyRoute"
import { ProtectedRoute } from "./ProtectedRoute"

export const AppRouter = () => {
    return (
        <Routes>
            <Route path="/" element={<LandingPage />} />

            {/* Rutas solo visibles sin estar logeado */}
            <Route element={<PublicOnlyRoute />}>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
            </Route>

            {/* Rutas solo visibles al estar logeado */}
            <Route element={<ProtectedRoute />}>
                <Route path="/dashboard" element={<DashboardPage />} />
            </Route>

            {/* Redireccion por defecto */}
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    )
}