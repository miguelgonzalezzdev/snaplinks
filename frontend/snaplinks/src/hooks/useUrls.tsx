import { useEffect, useState } from "react"
import type { Url, CreateUrlRequest } from '../types'
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

    const createUrl = async (newUrl: CreateUrlRequest) => {
        try {
            const created = await urlService.createUrl(newUrl)
            setUrls(prev => [created, ...prev]) 
            return { success: true, message: "URL creada correctamente" }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al crear la URL'
            setError(message)
            return { success: false, message }
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
        fetchUrls,
        createUrl
    }
}