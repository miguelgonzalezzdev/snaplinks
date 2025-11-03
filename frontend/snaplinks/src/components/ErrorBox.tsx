interface ErrorBoxProps {
    message: string;
    className?: string;
}

export default function ErrorBox({ message, className = "" }: ErrorBoxProps) {
    return (
        <div
            className={`flex items-center gap-3 bg-gray-800 border border-red-500 text-red-400 px-4 py-3 rounded-lg shadow-md text-sm ${className}`}
            role="alert"
        >
            <svg
                xmlns="http://www.w3.org/2000/svg"
                className="w-5 h-5 shrink-0"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                strokeWidth={2}
            >
                <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M12 9v2m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                />
            </svg>
            <span>{message}</span>
        </div>
    );
}
