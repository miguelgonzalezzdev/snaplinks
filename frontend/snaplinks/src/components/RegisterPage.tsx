import { Link } from "react-router";
import { useRegisterForm } from "../hooks/useRegisterForm";

export default function RegisterPage() {
    const {
        name,
        email,
        password,
        repeatPassword,
        error,
        isLoading,
        handleName,
        handleEmail,
        handlePassword,
        handleRepeatPassword,
        handleSubmit,
    } = useRegisterForm()

    return (
        <div 
            className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-gray-300 px-6 py-12"
            role="main"
            aria-label="Página de registro"
        >
            <div className="w-full max-w-md bg-gray-800 border border-gray-700 rounded-2xl shadow-2xl p-8" role="region" aria-labelledby="register-title">
                <div className="flex flex-col items-center mb-8">
                    <img
                        src="/snaplinks-logo.png"
                        alt="Logo de SnapLinks"
                        className="w-12 h-12 mb-3"
                    />
                    <h1 id="register-title" className="text-2xl font-bold text-gray-100">Registrarse</h1>
                    <p className="text-gray-400 mt-2 text-center text-sm">
                        Bienvenido a{" "}
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
                        <label htmlFor="name" className="block text-gray-300 mb-2 text-sm">Nombre</label>
                        <input
                            id="name"
                            type="text"
                            value={name}
                            onChange={handleName}
                            placeholder="Tu nombre"
                            className="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 placeholder-gray-500 focus:outline-none focus:border-indigo-500 transition-colors"
                        />
                    </div>
                    <div>
                        <label htmlFor="email" className="block text-gray-300 mb-2 text-sm">Correo electrónico</label>
                        <input
                            id="email"
                            type="email"
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
                    <div>
                        <label htmlFor="repeatPassword" className="block text-gray-300 mb-2 text-sm">Confirmar contraseña</label>
                        <input
                            id="repeatPassword"
                            type="password"
                            value={repeatPassword}
                            onChange={handleRepeatPassword}
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
                        {isLoading ? "Accediendo..." : "Crear cuenta"}
                    </button>
                </form>
                <div className="text-center mt-6 text-sm text-gray-400" aria-label="Enlace a inicio de sesión">
                    <p>
                        ¿Ya tienes una cuenta?{" "}
                        <Link
                            to="/login"
                            className="text-indigo-400 hover:text-indigo-300 font-medium transition-colors"
                        >
                            Inicia sesión
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}
