import { Navigate, Route, Routes } from "react-router"
import LandingPage from "../components/LandingPage"
import DashboardPage from "../components/DashboardPage"

export const AppRouter = () => {
    return (
        <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/dashboard" element={<DashboardPage />} />

            {/* Redirección por defecto */}
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    )
}