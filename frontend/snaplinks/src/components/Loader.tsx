export function Loader() {
    return (
        <div className="fixed inset-0 bg-neutral-50 dark:bg-neutral-800 bg-opacity-50 flex items-center flex-col gap-6 justify-center z-50">
            <div className="w-24 h-24 border-8 border-indigo-600 border-t-transparent rounded-full animate-spin" />
            <h1 className="text-xl text-neutral-900 dark:text-neutral-50 font-semibold">Cargando...</h1>
        </div>
    )
}