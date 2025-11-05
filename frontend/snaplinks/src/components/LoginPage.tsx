import { Link } from "react-router";
import { useLoginForm } from "../hooks/useLoginForm";

export default function LoginPage() {
    const {
        email,
        password,
        error,
        isLoading,
        handleEmail,
        handlePassword,
        handleSubmit,
    } = useLoginForm()

    return (
        <div 
            className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-gray-300 px-6 py-12"
            role="main"
            aria-label="Página de inicio de sesión"
        >
            <div role="region" aria-labelledby="login-title" className="w-full max-w-md bg-gray-800 border border-gray-700 rounded-2xl shadow-2xl p-8">
                <div className="flex flex-col items-center mb-8">
                    <img
                        src="/snaplinks-logo.png"
                        alt="Logo de SnapLinks"
                        className="w-12 h-12 mb-3"
                    />
                    <h1 id="login-title" className="text-2xl font-bold text-gray-100">Iniciar sesión</h1>
                    <p id="login-description" className="text-gray-400 mt-2 text-center text-sm">
                        Bienvenido de nuevo a{" "}
                        <Link
                            to="/"
                            className="text-indigo-400 hover:text-indigo-300 font-medium transition-colors"
                        >
                            SnapLinks
                        </Link>
                    </p>
                </div>
                <form onSubmit={handleSubmit} className="flex flex-col gap-5">
                    <div>
                        <label htmlFor="email" className="block text-gray-300 mb-2 text-sm">Correo electrónico</label>
                        <input
                            id="email"
                            type="text"
                            value={email}
                            onChange={handleEmail}
                            placeholder="tucorreo@ejemplo.com"
                            className="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 placeholder-gray-500 focus:outline-none focus:border-indigo-500 transition-colors"
                        />
                    </div>
                    <div>
                        <label htmlFor="password" className="block text-gray-300 mb-2 text-sm">Contraseña</label>
                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={handlePassword}
                            placeholder="••••••••"
                            className="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 placeholder-gray-500 focus:outline-none focus:border-indigo-500 transition-colors"
                        />
                    </div>

                    {error && (
                        <p id="loginError" className="text-red-600 text-sm text-center" role="alert" aria-live="assertive">{error}</p>
                    )}

                    <button
                        type="submit"
                        disabled={isLoading}
                        className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white py-3 rounded-lg font-semibold transition-all shadow-lg shadow-indigo-600/30"
                        aria-busy={isLoading}
                    >
                        {isLoading ? "Accediendo..." : "Iniciar sesión"}
                    </button>
                </form>
                <div className="text-center mt-6 text-sm text-gray-400" aria-label="Enlace a registro">
                    <p>
                        ¿No tienes cuenta?{" "}
                        <Link
                            to="/register"
                            className="text-indigo-400 hover:text-indigo-300 font-medium transition-colors"
                        >
                            Regístrate gratis
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}
