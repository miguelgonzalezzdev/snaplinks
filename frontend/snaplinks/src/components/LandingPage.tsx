import { useState } from "react";
import Footer from "./Footer";
import { Link } from "react-router";

export default function LandingPage() {
  const [url, setUrl] = useState("");
  const [shortened, setShortened] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleDemo = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!url.trim()) return;
    setLoading(true);
    setTimeout(() => {
      setShortened(`https://snap.li/${Math.random().toString(36).substring(7)}`);
      setLoading(false);
    }, 1200);
  };

  return (
    <div className="min-h-screen flex flex-col bg-gray-900 text-gray-100">

      <header className="border-b border-gray-800 bg-gray-900/70 backdrop-blur-lg sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-8 py-4 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-3">
            <img
              src="/snaplinks-logo.png"       
              alt="SnapLinks Logo"
              className="w-8 h-8 sm:w-10 sm:h-10" 
            />
            <h2 className="text-xl sm:text-2xl font-semibold text-gray-100">SnapLinks</h2>
          </Link>

          <nav className="flex items-center gap-6 text-md">
            <Link to="/login" className="hover:text-indigo-400 transition-colors">
              Iniciar sesión
            </Link>
            <Link to="/register" className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg transition-all">
              Registrarse
            </Link>
          </nav>
        </div>
      </header>

      <main className="flex flex-col">

        <section className="flex-1 flex flex-col lg:flex-row items-center justify-center px-4 py-20 lg:py-32 max-w-7xl mx-auto gap-10 lg:gap-18 overflow-hidden">
          <div className="flex-[1.3] text-center lg:text-left w-full">
            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-gray-100 mb-6 sm:mb-8 leading-tight tracking-tight">
              Acorta tus enlaces, <br className="hidden sm:block" /> comparte facilmente
            </h1>
            <div className="text-gray-300 text-lg sm:text-xl max-w-xl mx-auto lg:mx-0 mb-8 sm:mb-10 leading-relaxed">
              <p>
                Con <span className="text-indigo-400 font-medium">SnapLinks</span> puedes crear <span className="text-indigo-400 font-medium">enlaces cortos</span> y elegantes en segundos,
                fáciles de compartir en cualquier plataforma.
              </p>
              <p>
                Genera automáticamente un <span className="text-indigo-400 font-medium">código QR</span> para que tus enlaces se compartan
                de manera instantánea y sin complicaciones.
              </p>
            </div>
            <a
              href="#open-source"
              className="inline-block bg-indigo-600 hover:bg-indigo-700 text-white px-8 py-4 rounded-xl font-semibold transition-all shadow-lg shadow-indigo-600/30"
            >
              Regístrate gratis
            </a>
          </div>

          <div className="flex-[0.9] w-full bg-gray-800 border border-gray-700 rounded-2xl p-4 sm:p-8 lg:p-10 shadow-2xl">
            <h3 className="text-2xl sm:text-3xl font-semibold mb-4 sm:mb-5 text-center">
              Acortar URL
            </h3>
            <p className="text-gray-300 text-base text-center mb-6 sm:mb-8">
              Introduce una URL y observa cómo se acorta en segundos.
            </p>
            <form onSubmit={handleDemo} className="w-full">
              <div className="flex flex-col sm:flex-row w-full rounded-lg overflow-hidden border border-gray-700 focus-within:border-indigo-500 transition-colors">
                <input
                  type="url"
                  value={url}
                  onChange={(e) => setUrl(e.target.value)}
                  placeholder="https://ejemplo.com"
                  className="flex-1 px-4 sm:px-5 py-3 sm:py-4 bg-gray-900 text-gray-200 placeholder-gray-500 outline-none text-base sm:text-lg"
                />
                <button
                  type="submit"
                  disabled={loading}
                  className="bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white px-6 sm:px-8 py-3 sm:py-4 font-semibold text-base sm:text-lg transition-all cursor-pointer whitespace-nowrap"
                >
                  {loading ? "Acortando..." : "Acortar"}
                </button>
              </div>
            </form>

            {shortened && (
              <div className="mt-6 sm:mt-8 bg-gray-900 border border-gray-700 rounded-lg p-4 sm:p-5 text-center text-indigo-400 font-medium text-base sm:text-lg break-all">
                <a
                  href={shortened}
                  target="_blank"
                  className="hover:text-indigo-300 transition-colors"
                >
                  {shortened}
                </a>
              </div>
            )}
          </div>
        </section>

        <section className="py-32 px-8 border-t border-gray-800 bg-gray-900">
          <div className="max-w-7xl mx-auto flex flex-col lg:flex-row items-center gap-16">
            <div className="flex-1 w-full">
              <img
                src="/man-snaplinks.png"
                alt="Ilustración de acortador de enlaces"
                className="w-full max-w-md mx-auto"
              />
            </div>
            
            <div className="flex-1 text-left">
              <h3 className="text-3xl sm:text-4xl font-bold mb-6 leading-snug text-center lg:text-left">
                ¿Por qué usar SnapLinks?
              </h3>
              <p className="text-gray-300 text-lg mb-10 max-w-lg mx-auto lg:mx-0 leading-relaxed">
                Un acortador de enlaces como <span className="text-indigo-400 font-medium">SnapLinks</span> te permite compartir URLs de manera más elegante, segura y fácil de recordar. Ideal para redes sociales, campañas o proyectos personales.
              </p>

              <ul className="space-y-5 text-gray-300">
                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">Rápido y eficiente</h4>
                    <p className="text-gray-300">Tus enlaces se acortan en cuestión de segundos.</p>
                  </div>
                </li>

                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">Completamente gratis</h4>
                    <p className="text-gray-300">Disfruta de todas las funciones sin coste alguno.</p>
                  </div>
                </li>

                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">URLs limpias</h4>
                    <p className="text-gray-300">Perfectas para compartir en todas las plataformas.</p>
                  </div>
                </li>

                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">Seguimiento y control</h4>
                    <p className="text-gray-300">Analiza cuántos clics reciben tus enlaces.</p>
                  </div>
                </li>

                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">Seguridad</h4>
                    <p className="text-gray-300">Protege tus enlaces ocultando la URL original.</p>
                  </div>
                </li>

              </ul>
            </div>
          </div>
        </section>

        <section className="py-32 px-8 text-center border-t border-gray-800 bg-gray-900">
          <h3 className="text-3xl font-semibold text-gray-100 mb-6">
            SnapLinks es 100% Open Source
          </h3>
          <p className="text-gray-300 max-w-3xl mx-auto mb-10 text-lg leading-relaxed">
            SnapLinks es un proyecto de codigo abierto. Ideal para cualquier persona que quiera compartir enlaces de forma rapida y sin coste alguno.
          </p>
          <a
            href="https://github.com/miguelgonzalezzdev/snaplinks"
            target="_blank"
            className="inline-flex items-center gap-2 bg-gray-800 hover:bg-gray-700/50 border border-gray-700 hover:border-indigo-500 text-gray-300 hover:text-indigo-400 px-6 py-4 rounded-lg transition-all text-lg"
          >
            Ver en GitHub
          </a>
        </section>

      </main>

      <Footer />
    </div>
  );
}
