import { useEffect, useState } from "react"
import type { Url, CreateUrlRequest, UpdateUrlRequest, CreateDemoUrlRequest } from '../types'
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

    const editUrl = async (editUrl: UpdateUrlRequest) => {
        try {
            const updated = await urlService.updateUrl(editUrl)
            setUrls(prev =>
                prev.map(url => (url.id === updated.id ? updated : url))
            )
            return { success: true, message: "URL actualizada correctamente" }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al crear la URL'
            setError(message)
            return { success: false, message }
        }
    }

    const deleteUrl = async (id: number) => {
        try {
            await urlService.deleteUrl(id)
            setUrls(prev => prev?.filter(url => url.id !== id))
            return { success: true, message: "URL eliminada correctamente" }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al crear la URL'
            setError(message)
            return { success: false, message }
        }
    }

    const createDemoUrl = async (newDemoUrl: CreateDemoUrlRequest) => {
        try {
            const created = await urlService.createDemoUrl(newDemoUrl)
            setUrls(prev => [created, ...prev])
            return { success: true, url: created, message: "Demo URL creada correctamente" }
        } catch (err) {
            const message = err instanceof Error ? err.message : 'Error al crear la demo URL'
            setError(message)
            return { success: false, url: null, message }
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
        createUrl,
        editUrl,
        deleteUrl,
        createDemoUrl
    }
}