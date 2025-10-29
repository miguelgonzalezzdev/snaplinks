
interface UrlCardProps {
    name: string;
    shortUrl: string;
    originalUrl: string;
    qrCodeUrl: string;
    expirationDate: string;
}

export default function UrlCard({ name, shortUrl, originalUrl, qrCodeUrl, expirationDate }: UrlCardProps) {
    const copyToClipboard = (text: string) => navigator.clipboard.writeText(text);

    return (
        <div className="flex flex-col rounded-lg border shadow-none hover:shadow-lg transition-all border-gray-700 bg-gray-800 text-gray-300 hover:border-gray-600 hover:shadow-lg-light"
        >
            <div className="flex items-center justify-between rounded-t-md border-b px-5 py-3 border-gray-700 bg-gray-700">
                <span className="text-base font-medium text-gray-50">{name}</span>
                <button
                    onClick={(e) => {
                        e.preventDefault();
                        copyToClipboard(shortUrl);
                    }}
                    className="text-gray-500 hover:text-indigo-400 transition-colors"
                    title="Copiar enlace corto"
                >
                    {/* ICONO COPY PENDIENTE */}
                </button>
            </div>

            <div className="flex flex-col sm:flex-row items-center justify-between p-5 gap-4">
                {/* QR */}
                <div className="shrink-0">
                    <img
                        src={qrCodeUrl}
                        alt="Código QR"
                        className="w-28 h-28 rounded-md border border-gray-700 bg-gray-900 p-2"
                    />
                </div>

                {/* URL info */}
                <div className="flex flex-col gap-3 flex-1 text-sm">
                    {/* URL corta */}
                    <div>
                        <p className="text-gray-500 text-xs uppercase tracking-wide mb-1">URL corta</p>
                        <div className="flex items-center gap-2 text-indigo-400">
                            <a href={shortUrl} target="_blank" className="break-all underline">{shortUrl}</a>
                            <button
                                onClick={() => navigator.clipboard.writeText(shortUrl)}
                                className="text-gray-500 hover:text-indigo-400 transition-colors"
                                title="Copiar enlace corto"
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="size-4">
                                    <path strokeLinecap="round" strokeLinejoin="round" d="M8 16h8M8 12h8m-8-4h8m-2-4H6a2 2 0 00-2 2v12a2 2 0 002 2h12a2 2 0 002-2V8z" />
                                </svg>
                            </button>
                        </div>
                    </div>

                    {/* URL original */}
                    <div>
                        <p className="text-gray-500 text-xs uppercase tracking-wide mb-1">URL original</p>
                        <a href={originalUrl} target="_blank" className="text-gray-400 break-all underline">{originalUrl}</a>
                    </div>

                    {/* Fecha de expiración */}
                    <div>
                        <p className="text-gray-500 text-xs uppercase tracking-wide mb-1">Expiración</p>
                        <div className="flex items-center gap-2 text-gray-400">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="size-4 text-gray-500">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                            </svg>
                            <span>{expirationDate}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
