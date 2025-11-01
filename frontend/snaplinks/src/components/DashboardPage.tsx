import Footer from "./Footer";
import Header from "./Header";
import UrlCard from "./UrlCard";

export default function DashboardPage() {

    const urls = [
        {
            id: 1,
            name: "OpenAI Research",
            shortUrl: "https://snap.li/xyz456",
            originalUrl: "https://openai.com/research",
            qrCodeUrl: "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://snap.li/xyz456",
            expirationDate: "2025-12-31",
        },
        {
            id: 2,
            name: "Mi Portfolio",
            shortUrl: "https://snap.li/abc123",
            originalUrl: "https://tu-portfolio.com",
            qrCodeUrl: "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://snap.li/abc123",
            expirationDate: "2025-11-30",
        },
        {
            id: 3,
            name: "Documentación React",
            shortUrl: "https://snap.li/react789",
            originalUrl: "https://reactjs.org/docs/getting-started.html",
            qrCodeUrl: "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://snap.li/react789",
            expirationDate: "2025-10-31",
        },
    ];

    return (
        <>
            <Header />
            <main className="flex-1 flex flex-col items-center p-6">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full max-w-7xl">
                    {urls.map((url) => (
                        <UrlCard key={url.id} {...url} />
                    ))}
                </div>
            </main>
            <Footer />
        </>
    );
}