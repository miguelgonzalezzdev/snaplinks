import { useState } from "react";
import { useUrls } from "../hooks/useUrls";
import ErrorBox from "./ErrorBox";
import Footer from "./Footer";
import Header from "./Header";
import { PlusIcon } from "./icons/PlusIcon";
import { Loader } from "./Loader";
import UrlCard from "./UrlCard";
import ModalUrlForm from "./ModalUrlForm";

export default function DashboardPage() {
    const { urls, isLoading, error, createUrl } = useUrls()
    const [openModal, setOpenModal] = useState(false);

    const openModalForm = () => setOpenModal(true);
    const closeModal = () => setOpenModal(false);

    if (isLoading) return <Loader />

    return (
        <>
            <Header />
            <main className="flex-1 flex flex-col items-center min-h-dvh p-6 px-2 md:px-14">
                <div className="w-full mb-8 flex justify-end">
                    <button
                        onClick={openModalForm}
                        className="inline-flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-6 py-3 rounded-xl font-semibold text-sm sm:text-base transition-all shadow-md shadow-indigo-600/30 hover:shadow-indigo-700/40 hover:scale-[1.02]"
                    >
                        <PlusIcon size={24} />
                        Nueva URL
                    </button>
                </div>
                <ModalUrlForm
                    isOpen={openModal}
                    title="Crear nueva URL"
                    onClose={closeModal}
                    onSubmit={createUrl}
                />
                {error && (
                    <div className="w-full max-w-3xl mb-6">
                        <ErrorBox message={error} />
                    </div>
                )}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full">
                    {urls.map((url) => (
                        <UrlCard key={url.id} {...url} qrCodeUrl={url.qrCode} />
                    ))}
                </div>
            </main>
            <Footer />
        </>
    );
}