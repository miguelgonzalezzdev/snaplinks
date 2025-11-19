import { GitHubIcon } from "./icons/GitHubIcon";

export default function Footer() {
  return (
    <footer className="w-full mt-12 bg-gray-800 backdrop-blur border-t border-gray-700 text-sm text-gray-400" role="contentinfo" aria-label="Información del sitio y enlaces externos">
      <div className="max-w-6xl mx-auto px-6 py-12 flex flex-col lg:flex-row justify-between gap-8">
        <div className="flex flex-col items-center lg:items-start gap-2">
          <div className="flex items-center gap-3" aria-label="Identidad del proyecto">
            <img src="/snaplinks-logo.png" alt="Logotipo de SnapLinks" className="w-8 h-8" />
            <h4 className="text-gray-100 font-semibold text-lg">SnapLinks</h4>
          </div>
          <p>
            Desarrollado con <span role="img" aria-label="corazón blanco">🤍</span> por{" "}
            <a
              href="https://miguelgonzalezdev.es"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-indigo-400 transition-colors"
            >
              Miguel
            </a>
          </p>
        </div>
        <nav aria-label="Enlaces externos" className="flex flex-col sm:flex-row items-center gap-6 justify-center lg:justify-end">
          <ul className="flex items-center gap-6">
            <li>
              <a
                href="https://github.com/miguelgonzalezzdev/snaplinks"
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center gap-1 hover:text-indigo-400 transition-colors underline-offset-4 focus:outline-none focus:ring-2 focus:ring-indigo-500 rounded-sm"
                aria-label="Repositorio de SnapLinks en GitHub"
              >
                <GitHubIcon size={18} aria-hidden="true" />
                <span>GitHub</span>
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </footer>
  );
}
