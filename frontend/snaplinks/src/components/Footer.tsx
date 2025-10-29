export default function Footer() {
    return (
        <footer className="w-full mt-12 bg-gray-800 backdrop-blur border-t border-gray-700 text-sm">
            <div className="max-w-5xl mx-auto flex flex-col sm:flex-row items-center justify-center p-6">
                <p>
                    Desarrollado con 🤍 por{" "}
                    <a
                        href="https://miguelgonzalezzdev.github.io/portfolio/"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="hover:underline"
                    >
                        Miguel
                    </a>
                </p>
            </div>
        </footer>
    );
}
