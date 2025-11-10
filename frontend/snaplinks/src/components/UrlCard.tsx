import config from "../config/config";
import { ChartIcon } from "./icons/ChartIcon";
import { CopyIcon } from "./icons/CopyIcon";
import { DeleteIcon } from "./icons/DeleteIcon";
import { DownloadIcon } from "./icons/DownloadIcon";
import { EditIcon } from "./icons/EditIcon";
import { ShareIcon } from "./icons/ShareIcon";
import toast from "react-hot-toast"

interface UrlCardProps {
    id: number
    name: string
    shortCode: string
    originalUrl: string
    qrCodeUrl: string
    onEdit: () => void;
    onView: () => void;
    onDelete: () => void;
}

export default function UrlCard({ name, shortCode, originalUrl, qrCodeUrl, onEdit, onView, onDelete }: UrlCardProps) {
    const shortUrl = config.API_URL + '/u/' + shortCode

    const copyToClipboard = (text: string) => {
        navigator.clipboard.writeText(text)
        toast.success("Enlace copiado.");
    }

    const shareUrl = async () => {
        if (navigator.share) {
            try {
                await navigator.share({
                    title: name,
                    text: "Mira mi enlace:",
                    url: shortUrl,
                });
            } catch (err) {
                toast.error("Error compartiendo: " + err);
            }
        } else {
            toast.error("Tu navegador no permite compartir.");
        }
    }

    const shareQr = () => {
        const link = document.createElement('a')
        link.href = `data:image/png;base64,${qrCodeUrl}`
        link.download = `${name}-qr.png`
        link.click()
        toast.success("Código QR descargado.");
    }

    return (
        <div aria-label={`Tarjeta del enlace acortado: ${name}`} role="group" className="flex flex-col rounded-lg border transition-all border-gray-700 bg-gray-800 text-gray-300 hover:border-gray-600 hover:shadow-lg dark:hover:shadow-lg-light">
            <div className="flex items-center justify-between rounded-t-md border-b px-5 py-3 border-gray-700 bg-gray-700">
                <span className="text-base font-medium text-gray-50">{name}</span>
                <div className="flex items-center gap-2">
                    <button
                        onClick={onEdit}
                        className="p-2 rounded-md hover:bg-indigo-500/10 transition-colors text-gray-300 hover:text-indigo-500 flex items-center justify-center"
                        title="Editar enlace"
                        aria-label={`Editar ${name}`}
                    >
                        <EditIcon size={22} />
                    </button>
                    <button
                        onClick={onDelete}
                        className="p-2 rounded-md hover:bg-red-500/10 transition-colors text-gray-300 hover:text-red-400 flex items-center justify-center"
                        title="Eliminar enlace"
                        aria-label={`Eliminar ${name}`}
                    >
                        <DeleteIcon size={22} />
                    </button>
                </div>
            </div>
            <div className="flex flex-col sm:flex-row items-center justify-between p-5 gap-4">
                <div className="shrink-0 w-28 h-28 rounded-md overflow-hidden">
                    <img
                        src={`data:image/png;base64,${qrCodeUrl}`}
                        alt={`Código QR del enlace acortado: ${name}`}
                        className="w-full h-full object-cover"
                    />
                </div>
                <div className="flex flex-col gap-3 flex-1 text-sm">
                    <div>
                        <p className="text-gray-500 text-xs uppercase tracking-wide mb-1">URL corta</p>
                        <div className="flex items-center gap-2 text-indigo-400">
                            <a href={shortUrl} target="_blank" aria-label={`Abrir enlace corto: ${shortUrl}`} className="break-all underline">{shortUrl}</a>
                        </div>
                    </div>
                    <div>
                        <p className="text-gray-500 text-xs uppercase tracking-wide mb-1">URL original</p>
                        <a href={originalUrl} target="_blank" aria-label={`Abrir enlace original: ${originalUrl}`} className="text-gray-400 break-all underline">{originalUrl}</a>
                    </div>
                </div>
            </div>
            <div className="flex justify-start px-3 py-3 border-t border-gray-700 gap-3 flex-wrap">
                <button
                    onClick={onView}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1.5 rounded-lg font-medium text-sm transition-all shadow-sm hover:shadow-md flex items-center gap-2"
                    aria-label={`Ver estadísticas de: ${shortUrl}`}
                >
                    <ChartIcon size={18} />
                    Estadísticas
                </button>
                <button
                    onClick={() => copyToClipboard(shortUrl)}
                    className="bg-gray-700 hover:bg-gray-800/50 border border-gray-600 hover:border-indigo-500 text-gray-300 hover:text-indigo-400 px-3 py-1.5 rounded-lg font-medium text-sm transition-all flex items-center gap-2"
                    aria-label={`Copiar enlace acortado: ${shortUrl}`}
                >
                    <CopyIcon size={18} />
                    Copiar
                </button>
                <button
                    onClick={shareUrl}
                    className="bg-gray-700 hover:bg-gray-800/50 border border-gray-600 hover:border-indigo-500 text-gray-300 hover:text-indigo-400 px-3 py-1.5 rounded-lg font-medium text-sm transition-all flex items-center gap-2"
                    aria-label={`Compartir enlace: ${name}`}
                >
                    <ShareIcon size={18} />
                    Compartir
                </button>
                <button
                    onClick={shareQr}
                    className="bg-gray-700 hover:bg-gray-800/50 border border-gray-600 hover:border-indigo-500 text-gray-300 hover:text-indigo-400 px-3 py-1.5 rounded-lg font-medium text-sm transition-all flex items-center gap-2"
                    aria-label={`Descargar código QR del enlace: ${name}`}
                >
                    <DownloadIcon size={18} />
                    QR
                </button>
            </div>
        </div>
    );
}
