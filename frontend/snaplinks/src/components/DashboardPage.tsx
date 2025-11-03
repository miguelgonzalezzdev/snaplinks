import { useUrls } from "../hooks/useUrls";
import { useAuthStore } from "../store/useAuthStore";
import Footer from "./Footer";
import Header from "./Header";
import { Loader } from "./Loader";
import UrlCard from "./UrlCard";

export default function DashboardPage() {
    const accessToken = useAuthStore((state) => state.accessToken)
    const { urls, isLoading, error } = useUrls()

    if (isLoading) return <Loader />

    return (
        <>
            <Header />
            <main className="flex-1 flex flex-col items-center p-6">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full max-w-7xl">
                    {urls.map((url) => (
                        <UrlCard key={url.id} id={url.id} name={url.name} shortCode={url.shortCode} originalUrl={url.originalUrl} qrCodeUrl={url.qrCode} />
                    ))}
                </div>
            </main>
            <Footer />
        </>
    );
}