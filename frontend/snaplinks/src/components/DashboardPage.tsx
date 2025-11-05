import { useState } from "react";
import { useUrls } from "../hooks/useUrls";
import ErrorBox from "./ErrorBox";
import Footer from "./Footer";
import Header from "./Header";
import { PlusIcon } from "./icons/PlusIcon";
import { Loader } from "./Loader";
import UrlCard from "./UrlCard";
import ModalUrlForm from "./ModalUrlForm";

interface DataToEditUrl {
    id: number;
    name: string;
    originalUrl: string;
}

export default function DashboardPage() {
    const { urls, isLoading, error, createUrl, editUrl, deleteUrl } = useUrls()
    const [openModal, setOpenModal] = useState(false);
    const [editingUrl, setEditingUrl] = useState<DataToEditUrl | null>(null);

    const openModalForm = (url?: DataToEditUrl) => {
        setEditingUrl(url || null);
        setOpenModal(true);
    };

    const closeModal = () => {
        setOpenModal(false);
        setEditingUrl(null);
    };

    if (isLoading) return <Loader />

    return (
        <>
            <Header />
            <main 
                className="flex-1 flex flex-col items-center min-h-dvh p-6 px-4 md:px-14"
                role="main"
                aria-label="Panel principal del usuario"
            >
                <div className="w-full mb-8 flex justify-end">
                    <button
                        onClick={() => openModalForm()}
                        className="inline-flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-xl font-semibold text-md sm:text-base transition-all shadow-md shadow-indigo-600/30 hover:shadow-indigo-700/40 hover:scale-[1.02]"
                        aria-label="Crear una nueva URL acortada"
                    >
                        <PlusIcon size={24} />
                        Nueva URL
                    </button>
                </div>
                <ModalUrlForm
                    key={editingUrl ? editingUrl.id : "new"}
                    isOpen={openModal}
                    title={editingUrl ? "Editar URL" : "Crear nueva URL"}
                    idProp={editingUrl ? editingUrl.id.toString() : undefined}
                    nameProp={editingUrl?.name}
                    originalUrlProp={editingUrl?.originalUrl}
                    onClose={closeModal}
                    onSubmit={(data) => {
                        if (editingUrl) {
                            return editUrl({ id: editingUrl.id, ...data });
                        } else {
                            return createUrl(data);
                        }
                    }}
                />
                {error && (
                    <div className="w-full max-w-3xl mb-6" role="alert" aria-live="assertive">
                        <ErrorBox message={error} />
                    </div>
                )}
                {urls.length === 0 ? (
                    <section className="flex flex-col items-center justify-center text-center text-gray-400 py-20" aria-label="Listado de URLs creadas">
                        <h3 className="text-xl font-medium mb-3 text-gray-400">Vaya, aún no tienes URLs creadas...</h3>
                        <p className="text-gray-500 mb-6">Empieza añadiendo tu primer enlace corto.</p>
                        <button
                            onClick={() => openModalForm()}
                            className="inline-flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-2 rounded-lg font-medium text-sm transition-all shadow-md shadow-indigo-600/30 hover:shadow-indigo-700/40"
                            aria-label="Crear tu primer enlace acortado"
                        >
                            <PlusIcon size={20} />
                            Crear URL
                        </button>
                    </section>
                ) : (
                    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full" aria-label="Listado de URLs creadas">
                        {urls.map((url) => (
                            <UrlCard
                                key={url.id}
                                {...url}
                                qrCodeUrl={url.qrCode}
                                onEdit={() => openModalForm(url)}
                                onDelete={() => deleteUrl(url.id)}
                            />
                        ))}
                    </section>
                )}
            </main>
            <Footer />
        </>
    );
}