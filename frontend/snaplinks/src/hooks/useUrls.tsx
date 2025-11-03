import { useEffect, useState } from "react"
import type { Url } from '../types'
import { urlService } from "../services/urlService"

export const useUrls = () => {
    const [urls, setUrls] = useState<Url[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState("")

    const fetchUrls = async () => {

        setIsLoading(true)
        setError("")

        try {
            
            const data = await urlService.getUserUrls()
            setUrls(data)

        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al iniciar sesión'
            setError(message)
        } finally {
            setIsLoading(false)
        }
    }

    // Carga los datos al montar
    useEffect(() => {
        fetchUrls()
    }, [])

    return { 
        urls, 
        isLoading, 
        error, 
        fetchUrls 
    }
}