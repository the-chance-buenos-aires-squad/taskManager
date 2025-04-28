package org.buinos.presentation

class LoginUseCase {
    fun execute(username: String, password: String): Boolean {
        // Dummy validation: Accept any non-empty input
        return username.isNotBlank() && password.isNotBlank()
    }
}