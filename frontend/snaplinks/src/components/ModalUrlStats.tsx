import { useUrlStats } from "../hooks/useUrlStats";
import { CancelIcon } from "./icons/CancelIcon";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, BarChart, XAxis, YAxis, Bar } from "recharts";
import ErrorBox from "./ErrorBox";
import { useEffect } from "react";

interface ModalUrlStatsProps {
    isOpen: boolean;
    idUrl: number;
    onClose: () => void;
}

export default function ModalUrlStats({ isOpen, idUrl, onClose }: ModalUrlStatsProps) {
    const { stats, isLoading, error } = useUrlStats({ id: idUrl });

    // Cierra con tecla Escape
    useEffect(() => {
        const handleKeyDown = (e: KeyboardEvent) => {
            if (e.key === "Escape") {
                onClose();
            }
        };
        document.addEventListener("keydown", handleKeyDown);
        return () => document.removeEventListener("keydown", handleKeyDown);
    }, [isOpen, onClose]);

    if (!isOpen) return null;

    const COLORS = ["#6366f1", "#10b981", "#f59e0b", "#ef4444", "#8b5cf6"];

    const toChartData = (obj: Record<string, number>) =>
        Object.entries(obj).map(([name, value]) => ({ name, value }));

    const countries = toChartData(stats?.accessesByCountry || {});
    const browsers = toChartData(stats?.accessesByBrowser || {});
    const devices = toChartData(stats?.accessesByDeviceType || {});

    function timeAgo(dateString: string): string {
        if (!dateString || dateString == "") return "N/A";

        const date = new Date(dateString);
        const now = new Date();
        const diffMs = now.getTime() - date.getTime();

        const seconds = Math.floor(diffMs / 1000);
        const minutes = Math.floor(seconds / 60);
        const hours = Math.floor(minutes / 60);
        const days = Math.floor(hours / 24);

        if (seconds < 60) return "Hace unos segundos";
        if (minutes < 60) return `Hace ${minutes} minuto${minutes !== 1 ? "s" : ""}`;
        if (hours < 24) return `Hace ${hours} hora${hours !== 1 ? "s" : ""}`;
        if (days < 30) return `Hace ${days} día${days !== 1 ? "s" : ""}`;

        const months = Math.floor(days / 30);
        if (months < 12) return `Hace ${months} mes${months !== 1 ? "es" : ""}`;

        const years = Math.floor(months / 12);
        return `Hace ${years} año${years !== 1 ? "s" : ""}`;
    }

    if (isLoading) {
        return (
            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm" role="dialog" aria-modal="true" aria-labelledby="modal-loading-title">
                <div className="bg-gray-900 border border-gray-700 rounded-2xl shadow-2xl w-full max-w-5xl p-6 sm:p-8 m-4 relative animate-fadeIn h-full max-h-[95vh] overflow-y-auto flex flex-col items-center justify-center">
                    <button
                        onClick={onClose}
                        className="absolute top-2 right-2 p-2 rounded-md hover:bg-red-500/10 text-gray-400 hover:text-red-400 transition-colors"
                        aria-label="Cerrar modal"
                    >
                        <CancelIcon size={22} />
                    </button>
                    <div className="flex flex-col items-center space-y-4">
                        <div className="w-16 h-16 border-4 border-t-indigo-400 border-gray-700 rounded-full animate-spin"></div>
                        <p id="modal-loading-title" className="text-gray-400 text-lg font-medium">Cargando estadísticas...</p>
                    </div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm">
                <div
                    className="bg-gray-900 border border-gray-700 rounded-2xl shadow-2xl w-full max-w-5xl p-6 sm:p-8 m-4 relative animate-fadeIn h-full max-h-[95vh] overflow-y-auto"
                    role="dialog"
                    aria-modal="true"
                >
                    {error && (
                        <div className="w-full max-w-3xl mb-6" role="alert" aria-live="assertive">
                            <ErrorBox message={error} />
                        </div>
                    )}
                </div>
            </div>
        )
    }

    return (
        <div
            className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm"
            role="dialog"
            aria-modal="true"
            aria-labelledby="modal-stats-title"
        >
            <div
                className="bg-gray-900 border border-gray-700 rounded-2xl shadow-2xl w-full max-w-5xl p-6 sm:p-8 m-4 relative animate-fadeIn max-h-[95vh] overflow-y-auto"
                role="dialog"
                aria-modal="true"
            >
                <button
                    onClick={onClose}
                    className="absolute top-2 right-2 p-2 rounded-md hover:bg-red-500/10 text-gray-400 hover:text-red-400 transition-colors"
                    aria-label="Cerrar modal"
                >
                    <CancelIcon size={22} />
                </button>
                <h2 id="modal-stats-title" className="text-2xl font-semibold text-gray-100 mb-6 text-center">
                    Estadísticas de la URL
                </h2>
                <div className="space-y-8">

                    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4" role="group" aria-label="Resumen de estadísticas">
                        <div className="bg-gray-800 border border-gray-700 rounded-lg p-4 flex flex-col items-center">
                            <p className="text-gray-400 text-sm mb-1">Total de accesos</p>
                            <p className="text-4xl font-bold text-gray-100">{stats?.totalAccesses || 0}</p>
                        </div>
                        <div className="bg-gray-800 border border-gray-700 rounded-lg p-4 flex flex-col items-center">
                            <p className="text-gray-400 text-sm mb-1">Fecha creación</p>
                            <p className="text-gray-200 text-lg">{timeAgo(stats?.createdAt || "N/A")}</p>
                        </div>
                        <div className="bg-gray-800 border border-gray-700 rounded-lg p-4 flex flex-col items-center">
                            <p className="text-gray-400 text-sm mb-1">Último acceso</p>
                            <p className="text-gray-200 text-lg">{timeAgo(stats?.lastAccess || "N/A")}</p>
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

                        <div className="bg-gray-800 border border-gray-700 rounded-lg p-5 flex flex-col" role="region" aria-label="Accesos por país">
                            <h4 className="text-md font-semibold text-gray-200 mb-4 text-center">Accesos por país</h4>
                            <ResponsiveContainer width="100%" height={240}>
                                <BarChart data={countries}>
                                    <XAxis dataKey="name" stroke="#9ca3af" />
                                    <YAxis stroke="#9ca3af" allowDecimals={false} />
                                    <Tooltip
                                        contentStyle={{
                                            backgroundColor: "#1f2937",
                                            border: "1px solid #374151",
                                            borderRadius: "6px",
                                        }}
                                        itemStyle={{ color: "#e5e7eb" }}
                                    />
                                    <Bar dataKey="value" radius={[4, 4, 0, 0]}>
                                        {countries.map((_, i) => (
                                            <Cell key={i} fill={COLORS[i % COLORS.length]} />
                                        ))}
                                    </Bar>
                                </BarChart>
                            </ResponsiveContainer>
                        </div>

                        <div className="bg-gray-800 border border-gray-700 rounded-lg p-5 flex flex-col" role="region" aria-label="Accesos por navegador">
                            <h4 className="text-md font-semibold text-gray-200 mb-4 text-center">Accesos por navegador</h4>
                            <ResponsiveContainer width="100%" height={240}>
                                <BarChart data={browsers}>
                                    <XAxis dataKey="name" stroke="#9ca3af" />
                                    <YAxis stroke="#9ca3af" allowDecimals={false} />
                                    <Tooltip
                                        contentStyle={{
                                            backgroundColor: "#1f2937",
                                            border: "1px solid #374151",
                                            borderRadius: "6px",
                                        }}
                                        itemStyle={{ color: "#e5e7eb" }}
                                    />
                                    <Bar dataKey="value" radius={[4, 4, 0, 0]}>
                                        {browsers.map((_, i) => (
                                            <Cell key={i} fill={COLORS[i % COLORS.length]} />
                                        ))}
                                    </Bar>
                                </BarChart>
                            </ResponsiveContainer>
                        </div>

                        <div className="bg-gray-800 border border-gray-700 rounded-lg p-5 md:col-span-2 flex flex-col" role="region" aria-label="Accesos por tipos de dispositivo">
                            <h4 className="text-md font-semibold text-gray-200 mb-4 text-center">Accesos por dispositivo</h4>
                            <div className="flex-1">
                                <ResponsiveContainer width="100%" height={260}>
                                    <PieChart>
                                        <Pie
                                            data={devices}
                                            dataKey="value"
                                            nameKey="name"
                                            innerRadius={50}
                                            outerRadius={90}
                                            paddingAngle={5}
                                        >
                                            {devices.map((_, i) => (
                                                <Cell key={i} fill={COLORS[i % COLORS.length]} />
                                            ))}
                                        </Pie>
                                        <Tooltip
                                            contentStyle={{ backgroundColor: "#1f2937", border: "1px solid #374151", borderRadius: "6px" }}
                                            itemStyle={{ color: "#e5e7eb" }}
                                        />
                                    </PieChart>
                                </ResponsiveContainer>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
