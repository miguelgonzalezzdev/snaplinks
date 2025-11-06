import { useState } from "react"
import { useNavigate } from "react-router"
import { useAuthStore } from "../store/useAuthStore"

export const useLoginForm = () => {
    const { login } = useAuthStore()
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")
    const [isLoading, setIsLoading] = useState(false)
    const navigate = useNavigate()

    const handleEmail = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault()
        setEmail(event.target.value)
    }

    const handlePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault()
        setPassword(event.target.value)
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        setIsLoading(true)
        setError("")

        if (!email || !password) {
            setError("Por favor, completa todos los campos")
            setIsLoading(false)
            return
        }

        const res = await login({ email, password })

        if (res.success) {
            navigate("/dashboard")
        } else {
            setError("Usuario o contraseña incorrectos")
        }

        setIsLoading(false)
    }

    return {
        email,
        password,
        error,
        isLoading,
        handleEmail,
        handlePassword,
        handleSubmit,
    }
}
