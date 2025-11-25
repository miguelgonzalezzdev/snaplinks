import config from "../config/config";
import { useState } from "react";
import Footer from "./Footer";
import { Link } from "react-router";
import { GitHubIcon } from "./icons/GitHubIcon";
import { CutIcon } from "./icons/CutIcon";
import type { Url } from '../types.ts'
import toast from "react-hot-toast"
import { useUrls } from "../hooks/useUrls.tsx";
import { CopyIcon } from "./icons/CopyIcon.tsx";
import { DownloadIcon } from "./icons/DownloadIcon.tsx";
import { ShareIcon } from "./icons/ShareIcon.tsx";

export default function LandingPage() {
  const { createDemoUrl } = useUrls()
  const [inputUrl, setInputUrl] = useState("");
  const [demoUrl, setDemoUrl] = useState<Url | null>(null);
  const [loading, setLoading] = useState(false);

  const shortUrl = config.API_URL + '/u/'

  const handleUrlDemo = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault()
    setInputUrl(event.target.value)
  }

  const handleSubmitDemo = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)

    if (!inputUrl.trim()) {
      setLoading(false)
      toast.error("Introduce una URL válida.");
      return
    }

    const result = await createDemoUrl({ originalUrl: inputUrl.trim() })

    if (result.success && result.url) {
      setDemoUrl(result.url)
      toast.success("URL creada correctamente.")
    } else {
      toast.error(result.message || "Error al crear la URL.")
    }

    setLoading(false)
  }

  const copyToClipboard = () => {

    if (!demoUrl) return

    const url = shortUrl + demoUrl.shortCode
    navigator.clipboard.writeText(url)
    toast.success("Enlace copiado.");
  }

  const shareUrl = async () => {
    if (navigator.share && demoUrl) {

      const url = shortUrl + demoUrl.shortCode

      try {
        await navigator.share({
          text: "Mira mi enlace:",
          url: url,
        });
      } catch (err) {
        toast.error("Error compartiendo: " + err);
      }
    } else {
      toast.error("Tu navegador no permite compartir.");
    }
  }

  const shareQr = () => {

    if (!demoUrl) return

    const link = document.createElement('a')
    link.href = `data:image/png;base64,${demoUrl.qrCode}`
    link.download = "url-acortada-qr.png"
    link.click()
    toast.success("Código QR descargado.");
  }

  return (
    <div className="min-h-screen flex flex-col bg-gray-900 text-gray-100" role="document">

      <header className="border-b border-gray-800 bg-gray-900/70 backdrop-blur-lg sticky top-0 z-50" role="banner">
        <div className="max-w-7xl mx-auto px-8 py-4 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-3" aria-label="Ir al inicio">
            <img
              src="/snaplinks-logo.webp"
              alt="Logo de SnapLinks"
              className="w-8 h-8 sm:w-10 sm:h-10"
            />
            <h2 className="text-xl sm:text-2xl font-semibold text-gray-100 hidden sm:block">SnapLinks</h2>
          </Link>

          <nav className="flex items-center gap-6 text-md" aria-label="Navegación principal">
            <Link to="/login" className="hover:text-indigo-400 transition-colors">
              Iniciar sesión
            </Link>
            <Link to="/register" className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg transition-all">
              Registrarse
            </Link>
          </nav>
        </div>
      </header>

      <main className="flex flex-col" role="main" aria-label="Página principal de SnapLinks">

        <section className="flex-1 flex flex-col lg:flex-row items-center justify-center px-4 py-20 lg:py-32 max-w-7xl mx-auto gap-10 lg:gap-18 overflow-hidden" aria-labelledby="hero-title">
          <div className="flex-[1.3] text-center lg:text-left w-full">
            <h1 id="hero-title" className="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-gray-100 mb-6 sm:mb-8 leading-tight tracking-tight">
              Acorta tus enlaces, <br className="hidden sm:block" /> comparte facilmente
            </h1>
            <div id="hero-description" className="text-gray-300 text-lg sm:text-xl max-w-xl mx-auto lg:mx-0 mb-8 sm:mb-10 leading-relaxed text-left">
              <p>
                Con <span className="text-indigo-400 font-medium">SnapLinks</span> puedes crear <span className="text-indigo-400 font-medium">enlaces cortos</span> y elegantes en segundos,
                fáciles de compartir en cualquier plataforma.
              </p>
              <p>
                Genera automáticamente un <span className="text-indigo-400 font-medium">código QR</span> para que tus enlaces se compartan
                de manera instantánea y sin complicaciones.
              </p>
            </div>
            <Link
              to="/login"
              className="inline-block bg-indigo-600 hover:bg-indigo-700 text-white px-8 py-4 rounded-xl font-semibold transition-all shadow-lg shadow-indigo-600/30"
            >
              Regístrate gratis
            </Link>
          </div>

          <div className="flex-[0.9] md:w-xl bg-gray-800 border border-gray-700 rounded-2xl p-4 sm:p-8 lg:p-10 shadow-2xl" role="region" aria-labelledby="demo-form-title">
            <h3 id="demo-form-title" className="text-2xl sm:text-3xl font-semibold mb-4 sm:mb-5 text-center">
              Acortar URL
            </h3>
            <p id="demo-form-description" className="text-gray-300 text-base text-center mb-6 sm:mb-8">
              Introduce una URL y observa cómo se acorta en segundos.
            </p>
            <form onSubmit={handleSubmitDemo} className="w-full" aria-describedby="demo-form-description">
              <div className="flex flex-col sm:flex-row w-full rounded-lg overflow-hidden border border-gray-700 focus-within:border-indigo-500 transition-colors">
                <input
                  id="demo-url"
                  type="url"
                  value={inputUrl}
                  onChange={handleUrlDemo}
                  placeholder="https://ejemplo.com"
                  className="flex-1 px-4 sm:px-5 py-3 sm:py-4 bg-gray-900 text-gray-200 placeholder-gray-500 outline-none text-base"
                />
                <button
                  type="submit"
                  disabled={loading}
                  className="inline-flex items-center justify-center gap-2 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white px-2 sm:px-4 py-3 sm:py-4 font-semibold text-base transition-all cursor-pointer whitespace-nowrap"
                  aria-busy={loading}
                >
                  {!loading && <CutIcon size={22} aria-hidden="true" />}
                  {loading ? "Acortando..." : "Acortar"}
                </button>
              </div>
            </form>

            {demoUrl && (
              <div
                className="mt-8 sm:mt-10 bg-gray-900 border border-gray-700 rounded-2xl overflow-hidden"
                role="status"
                aria-live="polite"
              >
                <div className="py-6 sm:py-8 px-4 sm:p-8 text-center flex flex-col items-center gap-5">
                  <div className="flex flex-col sm:flex-row items-center justify-center gap-6 w-full">
                    <div className="shrink-0 w-32 h-32 rounded-xl overflow-hidden">
                      <img
                        src={`data:image/png;base64,${demoUrl.qrCode}`}
                        alt="Código QR del enlace acortado"
                        className="w-full h-full object-contain"
                      />
                    </div>
                    <div className="text-center sm:text-left">
                      <p className="text-gray-400 text-sm mb-1">Enlace acortado:</p>
                      <a
                        href={shortUrl + demoUrl.shortCode}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-indigo-400 hover:text-indigo-300 font-medium break-all text-base transition-colors"
                      >
                        {shortUrl + demoUrl.shortCode}
                      </a>
                    </div>
                  </div>
                </div>
                <div className="flex justify-start px-4 md:px-8 py-4 border-t border-gray-700 gap-3 flex-wrap bg-gray-850">
                  <button
                    onClick={copyToClipboard}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1.5 rounded-lg font-medium text-sm transition-all shadow-sm hover:shadow-md flex items-center gap-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:ring-offset-gray-900"
                    aria-label={`Copiar enlace acortado: ${shortUrl + demoUrl.shortCode}`}
                  >
                    <CopyIcon size={18} aria-hidden="true" />
                    <span className="text-sm">Copiar</span>
                  </button>

                  <button
                    onClick={shareUrl}
                    className="bg-gray-700 hover:bg-gray-800/50 border border-gray-600 hover:border-indigo-500 text-gray-300 hover:text-indigo-400 px-3 py-1.5 rounded-lg font-medium text-sm transition-all flex items-center gap-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:ring-offset-gray-900"
                    aria-label={`Compartir enlace: ${shortUrl + demoUrl.shortCode}`}
                  >
                    <ShareIcon size={18} aria-hidden="true" />
                    <span>Compartir</span>
                  </button>

                  <button
                    onClick={shareQr}
                    className="bg-gray-700 hover:bg-gray-800/50 border border-gray-600 hover:border-indigo-500 text-gray-300 hover:text-indigo-400 px-3 py-1.5 rounded-lg font-medium text-sm transition-all flex items-center gap-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:ring-offset-gray-900"
                    aria-label={`Descargar código QR del enlace: ${shortUrl + demoUrl.shortCode}`}
                  >
                    <DownloadIcon size={18} aria-hidden="true" />
                    <span>QR</span>
                  </button>
                </div>
              </div>
            )}
          </div>
        </section>

        <section className="py-32 px-8 border-t border-gray-800 bg-gray-900" aria-labelledby="features-title">
          <div className="max-w-7xl mx-auto flex flex-col lg:flex-row items-center gap-16">
            <div className="flex-1 w-full">
              <img
                src="/man-snaplinks.webp"
                alt="Ilustración que representa el uso del acortador de enlaces"
                className="w-full max-w-md mx-auto"
              />
            </div>

            <div className="flex-1 text-left">
              <h3 id="features-title" className="text-3xl sm:text-4xl font-bold mb-6 leading-snug text-center lg:text-left">
                ¿Por qué usar SnapLinks?
              </h3>
              <p className="text-gray-300 text-lg mb-10 max-w-lg mx-auto lg:mx-0 leading-relaxed">
                Un acortador de enlaces como <span className="text-indigo-400 font-medium">SnapLinks</span> te permite compartir URLs de manera más elegante, segura y fácil de recordar. Ideal para redes sociales, campañas o proyectos personales.
              </p>

              <ul className="space-y-5 text-gray-300" role="list">
                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0" aria-hidden="true"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">Rápido y eficiente</h4>
                    <p className="text-gray-300">Tus enlaces se acortan en cuestión de segundos.</p>
                  </div>
                </li>

                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0" aria-hidden="true"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">Completamente gratis</h4>
                    <p className="text-gray-300">Disfruta de todas las funciones sin coste alguno.</p>
                  </div>
                </li>

                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0" aria-hidden="true"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">URLs limpias</h4>
                    <p className="text-gray-300">Perfectas para compartir en todas las plataformas.</p>
                  </div>
                </li>

                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0" aria-hidden="true"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">Seguimiento y control</h4>
                    <p className="text-gray-300">Analiza cuántos clics reciben tus enlaces.</p>
                  </div>
                </li>

                <li className="flex items-start gap-3">
                  <span className="mt-2 w-2.5 h-2.5 rounded-full bg-indigo-500 shrink-0" aria-hidden="true"></span>
                  <div>
                    <h4 className="text-lg font-semibold text-gray-100">Seguridad</h4>
                    <p className="text-gray-300">Protege tus enlaces ocultando la URL original.</p>
                  </div>
                </li>

              </ul>
            </div>
          </div>
        </section>

        <section id="open-source" className="py-32 px-8 text-center border-t border-gray-800 bg-gray-900" aria-labelledby="opensource-title">
          <h3 id="opensource-title" className="text-3xl font-semibold text-gray-100 mb-6">
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
            <GitHubIcon size={22} aria-hidden="true" />
            Ver en GitHub
          </a>
        </section>

      </main>

      <Footer />
    </div>
  );
}
