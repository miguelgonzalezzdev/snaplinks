import { Link } from "react-router";
import { useAuthStore } from "../store/useAuthStore";

export default function Header() {
    const logoutStore = useAuthStore((state) => state.logout)

    const handleLogout = () => {
        logoutStore()
    }

    return (
        <header className="border-b border-gray-800 bg-gray-900/70 backdrop-blur-lg sticky top-0 z-50" role="banner">
            <div className="mx-auto px-4 md:px-14 py-4 flex items-center justify-between">
                <Link to="/dashboard" className="flex items-center gap-3">
                    <img
                        src="/snaplinks-logo.png"
                        alt="Logo de SnapLinks - acortador de enlaces"
                        className="w-8 h-8 sm:w-10 sm:h-10"
                    />
                    <h2 className="text-xl sm:text-2xl font-semibold text-gray-100 hidden sm:block">SnapLinks</h2>
                </Link>
                <nav aria-label="Menú principal">
                    <ul className="flex items-center gap-4 text-md md:text-lg">
                        <li>
                            <button
                                onClick={handleLogout}
                                className="hover:text-indigo-400 transition-colors p-2 rounded-md hover:bg-indigo-500/10 cursor-pointer bg-transparent text-inherit"
                                aria-label="Cerrar sesión"
                            >
                                Salir
                            </button>
                        </li>
                    </ul>
                </nav>

            </div>
        </header>
    );
}
