export default function Header() {
    return (
        <header className="w-full bg-gray-800 backdrop-blur border-b border-gray-700 shadow-sm">
            <div className="max-w-5xl mx-auto flex items-center justify-between p-4">
                <a href="/" className="flex items-center gap-2">
                    <h1 className="text-2xl font-bold">SnapLinks</h1>
                </a>

                <nav className="flex gap-6 text-gray-300 text-md">
                    <a href="#" className="hover:text-gray-100">Inicio</a>
                    <a href="#" className="hover:text-gray-100">Estadísticas</a>
                    <a href="#" className="hover:text-gray-100">Salir</a>
                </nav>
            </div>
        </header>
    );
}
