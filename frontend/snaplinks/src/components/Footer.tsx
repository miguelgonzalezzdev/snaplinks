import { GitHubIcon } from "./icons/GitHubIcon";

export default function Footer() {
  return (
    <footer className="w-full mt-12 bg-gray-800 backdrop-blur border-t border-gray-700 text-sm text-gray-400">
      <div className="max-w-6xl mx-auto px-6 py-12 flex flex-col lg:flex-row justify-between gap-8">
        <div className="flex flex-col items-center lg:items-start gap-2">
          <div className="flex items-center gap-3">
            <img src="/snaplinks-logo.png" alt="SnapLinks Logo" className="w-8 h-8" />
            <h4 className="text-gray-100 font-semibold text-lg">SnapLinks</h4>
          </div>
          <p>
            Desarrollado con 🤍 por{" "}
            <a
              href="https://miguelgonzalezzdev.github.io/portfolio/"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-indigo-400 transition-colors"
            >
              Miguel
            </a>
          </p>
        </div>
        <div className="flex flex-col sm:flex-row items-center gap-6 justify-center lg:justify-end">
          <a href="https://github.com/miguelgonzalezzdev/snaplinks" target="_blank" className="flex items-center gap-1 hover:text-indigo-400 transition-colors">
            <GitHubIcon size={18} />GitHub
          </a>
        </div>
      </div>
    </footer>
  );
}
