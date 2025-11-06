import { useState } from "react"
import { useNavigate } from "react-router"
import { useAuthStore } from "../store/useAuthStore"

export const useRegisterForm = () => {
    const { register } = useAuthStore()
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [repeatPassword, setRepeatPassword] = useState("")
    const [error, setError] = useState("")
    const [isLoading, setIsLoading] = useState(false)
    const navigate = useNavigate()

    const handleName = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault()
        setName(event.target.value)
    }

    const handleEmail = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault()
        setEmail(event.target.value)
    }

    const handlePassword = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault()
        setPassword(event.target.value)
    }

    const handleRepeatPassword = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault()
        setRepeatPassword(event.target.value)
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        setIsLoading(true)
        setError("")

        if (!name || !email || !password || !repeatPassword) {
            setError("Por favor, completa todos los campos")
            setIsLoading(false)
            return
        }

        if (password !== repeatPassword) {
            setError("Las contraseñas no coinciden")
            setIsLoading(false)
            return
        }

        const res = await register({ name, email, password })

        if (res.success) {
            navigate("/dashboard")
        } else {
            setError("Error al registrar usuario")
        }

        setIsLoading(false)
    }

    return {
        name,
        email,
        password,
        repeatPassword,
        error,
        isLoading,
        handleName,
        handleEmail,
        handlePassword,
        handleRepeatPassword,
        handleSubmit,
    }
}
