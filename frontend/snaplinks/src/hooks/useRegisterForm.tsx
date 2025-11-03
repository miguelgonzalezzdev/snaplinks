import { useState } from "react"
import { useNavigate } from "react-router"
import { useAuthStore } from "../store/useAuthStore"

export const useRegisterForm = () => {
    const { register } = useAuthStore()
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
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

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        setIsLoading(true)
        setError("")

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
        error,
        isLoading,
        handleName,
        handleEmail,
        handlePassword,
        handleSubmit,
    }
}
