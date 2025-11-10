import { useEffect, useState } from "react";
import type { UrlId, UrlStats } from "../types"
import { urlService } from "../services/urlService";

interface UrlStatsProps {
    id: UrlId
}

export const useUrlStats = ({ id }: UrlStatsProps) => {
    const [stats, setStats] = useState<UrlStats | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchUrlStats = async ({ id }: { id: UrlId }) => {
            if (!id) return;

            setIsLoading(true)
            setError("")
    
            try {
    
                const data = await urlService.getUrlStats(id)
                setStats(data)
    
            } catch (err) {
                const message = err instanceof Error ? err.message : 'Error al obtener las estadísticas'
                setError(message)
            } finally {
                setIsLoading(false)
            }
        }

    useEffect(() => {
        fetchUrlStats({ id })
    }, [id])

    return { stats, isLoading, error };
}
