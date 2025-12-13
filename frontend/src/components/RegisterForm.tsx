import React, { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { api } from '../services/apiRequest'
import { AxiosError } from 'axios'

// Zod schema prema RegisterRequest
const registerSchema = z.object({
    username: z.string().min(2, 'Username must be at least 2 characters'),
    password: z.string().min(2, 'Password must be at least 6 characters'),
    email: z.email('Invalid email'),
    fullName: z.string().min(2, 'Full name must be at least 2 characters'),
    dateOfBirth: z.string().refine(
        (val) => !isNaN(Date.parse(val)),
        'Invalid date'
    ),
})

type RegisterFormInputs = z.infer<typeof registerSchema>

const RegisterForm: React.FC = () => {
    const [successMessage, setSuccessMessage] = useState<string | null>(null)
    const [errorMessage, setErrorMessage] = useState<string | null>(null)

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm<RegisterFormInputs>({
        resolver: zodResolver(registerSchema),
    })

    const onSubmit = async (data: RegisterFormInputs) => {
        try {
            // Pošalji ispravan payload backendu
            const res = await api.post('/auth/register', {
                username: data.username,
                password: data.password,
                email: data.email,
                fullName: data.fullName,
                dateOfBirth: data.dateOfBirth, // može biti string u formatu YYYY-MM-DD
            })

            setSuccessMessage(res.data.message)
            setErrorMessage(null)
        } catch (error) {
            const err = error as AxiosError<{ message?: string }>;
            setErrorMessage(err.response?.data?.message || "Registration failed");
            setSuccessMessage(null)
        }
    }

    return (
        <div className="register-form">
            <h2>Register</h2>
            {successMessage && <div style={{ color: 'green' }}>{successMessage}</div>}
            {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}

            <form onSubmit={handleSubmit(onSubmit)}>
                {/* Username */}
                <div>
                    <label>Username</label>
                    <input type="text" {...register('username')} />
                    {errors.username && <p>{errors.username.message}</p>}
                </div>

                {/* Full Name */}
                <div>
                    <label>Full Name</label>
                    <input type="text" {...register('fullName')} />
                    {errors.fullName && <p>{errors.fullName.message}</p>}
                </div>

                {/* Email */}
                <div>
                    <label>Email</label>
                    <input type="email" {...register('email')} />
                    {errors.email && <p>{errors.email.message}</p>}
                </div>

                {/* Password */}
                <div>
                    <label>Password</label>
                    <input type="password" {...register('password')} />
                    {errors.password && <p>{errors.password.message}</p>}
                </div>

                {/* Date of Birth */}
                <div>
                    <label>Date of Birth</label>
                    <input type="date" {...register('dateOfBirth')} />
                    {errors.dateOfBirth && <p>{errors.dateOfBirth.message}</p>}
                </div>

                <button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? 'Registering...' : 'Register'}
                </button>
            </form>
        </div>
    )
}

export default RegisterForm
