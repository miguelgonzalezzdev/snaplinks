import { useUrls } from "../hooks/useUrls";
import ErrorBox from "./ErrorBox";
import Footer from "./Footer";
import Header from "./Header";
import { Loader } from "./Loader";
import UrlCard from "./UrlCard";

export default function DashboardPage() {
    const { urls, isLoading, error } = useUrls()

    if (isLoading) return <Loader />

    return (
        <>
            <Header />
            <main className="flex-1 flex flex-col items-center p-6">
                {error && (
                    <div className="w-full max-w-3xl mb-6">
                        <ErrorBox message={error} />
                    </div>
                )}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full px-2 md:px-14">
                    {urls.map((url) => (
                        <UrlCard key={url.id} id={url.id} name={url.name} shortCode={url.shortCode} originalUrl={url.originalUrl} qrCodeUrl={url.qrCode} />
                    ))}
                </div>
            </main>
            <Footer />
        </>
    );
}