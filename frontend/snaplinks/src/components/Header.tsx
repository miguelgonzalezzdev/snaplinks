export default function Header() {
    return (
        <header className="border-b border-gray-800 bg-gray-900/70 backdrop-blur-lg sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-8 py-4 flex items-center justify-between">
                <div className="flex items-center gap-3">
                    <img
                        src="/snaplinks-logo.png"
                        alt="SnapLinks Logo"
                        className="w-8 h-8 sm:w-10 sm:h-10"
                    />
                    <h2 className="text-xl sm:text-2xl font-semibold text-gray-100">SnapLinks</h2>
                </div>

                <nav className="flex items-center gap-6 text-md">
                    <a href="#" className="hover:text-indigo-400 transition-colors">
                        Inicio
                    </a>
                    <a href="#" className="hover:text-indigo-400 transition-colors">
                        Estadísticas
                    </a>
                    <a href="#" className="hover:text-indigo-400 transition-colors">
                        Salir
                    </a>
                </nav>
            </div>
        </header>
    );
}
