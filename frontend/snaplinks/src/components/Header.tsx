import { Link } from "react-router";
import { useAuthStore } from "../store/useAuthStore";

export default function Header() {
    const logoutStore = useAuthStore((state) => state.logout)

    const handleLogout = () => {
        logoutStore()
    }

    return (
        <header className="border-b border-gray-800 bg-gray-900/70 backdrop-blur-lg sticky top-0 z-50">
            <div className="mx-auto px-4 md:px-14 py-4 flex items-center justify-between">
                <Link to="/dashboard" className="flex items-center gap-3">
                    <img
                        src="/snaplinks-logo.png"
                        alt="SnapLinks Logo"
                        className="w-8 h-8 sm:w-10 sm:h-10"
                    />
                    <h2 className="text-xl sm:text-2xl font-semibold text-gray-100 hidden sm:block">SnapLinks</h2>
                </Link>

                <nav className="flex items-center gap-4 text-md md:text-lg">
                    <Link to="/dashboard" className="hover:text-indigo-400 transition-colors p-2 rounded-md hover:bg-indigo-500/10">
                        Inicio
                    </Link>
                    <Link to="/" className="hover:text-indigo-400 transition-colors p-2 rounded-md hover:bg-indigo-500/10">
                        Estadísticas
                    </Link>
                    <p onClick={handleLogout} className="hover:text-indigo-400 transition-colors cursor-pointer p-2 rounded-md hover:bg-indigo-500/10">
                        Salir
                    </p>
                </nav>
            </div>
        </header>
    );
}
