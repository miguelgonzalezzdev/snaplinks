import { Link } from "react-router";

export default function Header() {
    return (
        <header className="border-b border-gray-800 bg-gray-900/70 backdrop-blur-lg sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-8 py-4 flex items-center justify-between">
                <Link to="/dashboard" className="flex items-center gap-3">
                    <img
                        src="/snaplinks-logo.png"
                        alt="SnapLinks Logo"
                        className="w-8 h-8 sm:w-10 sm:h-10"
                    />
                    <h2 className="text-xl sm:text-2xl font-semibold text-gray-100 hidden sm:block">SnapLinks</h2>
                </Link>

                <nav className="flex items-center gap-6 text-md">
                    <Link to="/dashboard" className="hover:text-indigo-400 transition-colors">
                        Inicio
                    </Link>
                    <Link to="/" className="hover:text-indigo-400 transition-colors">
                        Estadísticas
                    </Link>
                    <Link to="/" className="hover:text-indigo-400 transition-colors">
                        Salir
                    </Link>
                </nav>
            </div>
        </header>
    );
}
