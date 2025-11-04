import { useState } from "react";
import { CancelIcon } from "./icons/CancelIcon";
import toast from "react-hot-toast"

interface ModalUrlFormProps {
    isOpen: boolean;
    title: string;
    nameProp?: string;
    originalUrlProp?: string;
    onClose: () => void;
    onSubmit: (data: { name: string; originalUrl: string }) => Promise<{ success: boolean; message: string }>;
}

export default function ModalUrlForm({ isOpen, title, nameProp, originalUrlProp, onClose, onSubmit }: ModalUrlFormProps) {
    const [name, setName] = useState(nameProp || "")
    const [originalUrl, setOriginalUrl] = useState(originalUrlProp || "")
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState("")

    if (!isOpen) return null

    const handleName = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault()
        setName(event.target.value)
    }

    const handleOriginalUrl = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault()
        setOriginalUrl(event.target.value)
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setError("")
        setIsLoading(true)

        if(!name || !originalUrl || name=="" || originalUrl=="") {
            setError("Por favor, completa todos los campos.")
            setIsLoading(false)
            
            return
        }

        const result = await onSubmit({
            name: name,
            originalUrl: originalUrl,
        })

        if (result.success) {
            setName("")
            setOriginalUrl("")
            onClose()
            toast.success("URL creada correctamente.");
        }else{
            toast.error("Error crear la URL.");
        }

        setIsLoading(false)
    }

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm">
            <div className="bg-gray-800 border border-gray-700 rounded-xl shadow-2xl w-full max-w-lg p-8 animate-fadeIn relative">
                <button
                    onClick={onClose}
                    className="absolute top-1 right-1 p-2 rounded-md hover:bg-red-500/10 text-gray-300 hover:text-red-400 transition-colors"
                    aria-label="Cerrar modal"
                >
                    <CancelIcon size={22} />
                </button>

                <h2 className="text-xl font-semibold text-gray-100 mb-4 text-center">
                    {title}
                </h2>

                <form onSubmit={handleSubmit} className="flex flex-col gap-5">
                    <div>
                        <label className="block text-sm text-gray-300 mb-1">Nombre</label>
                        <input
                            type="text"
                            value={name}
                            onChange={handleName}
                            required
                            placeholder="Ejemplo: Mi enlace de GitHub"
                            className="w-full px-4 py-3 rounded-lg bg-gray-900 border border-gray-700 text-gray-100 placeholder-gray-500 focus:outline-none focus:border-indigo-500 transition-all"
                        />
                    </div>

                    <div>
                        <label className="block text-sm text-gray-300 mb-1">URL original</label>
                        <input
                            type="url"
                            value={originalUrl}
                            onChange={handleOriginalUrl}
                            required
                            placeholder="https://tusitio.com/enlace"
                            className="w-full px-4 py-3 rounded-lg bg-gray-900 border border-gray-700 text-gray-100 placeholder-gray-500 focus:outline-none focus:border-indigo-500 transition-all"
                        />
                    </div>

                    {error && (
                        <p id="loginError" className="text-red-600 text-sm text-center">{error}</p>
                    )}

                    <button
                        type="submit"
                        className="mt-2 inline-block bg-indigo-600 hover:bg-indigo-700 text-white px-8 py-3 rounded-xl font-semibold transition-all shadow-lg shadow-indigo-600/30"
                    >
                        {isLoading ? "Guardando..." : "Guardar cambios"}
                    </button>
                </form>
            </div>
        </div>
    );
}
