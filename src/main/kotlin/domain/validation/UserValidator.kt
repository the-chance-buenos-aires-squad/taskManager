package domain.validation

import domain.customeExceptions.InvalidConfirmPasswordException
import domain.customeExceptions.InvalidLengthPasswordException
import domain.customeExceptions.PasswordEmptyException
import domain.customeExceptions.UserNameEmptyException

class UserValidator {

    fun validateUsername(username: String) {
        if (username.isBlank()) throw UserNameEmptyException()
    }

    private fun isPasswordEmpty(password: String) {
        if (password.isBlank()) throw PasswordEmptyException()
    }

    fun validatePassword(password: String, confirmPassword: String) {

        isPasswordEmpty(password)
        isPasswordEmpty(confirmPassword)

        if (password.length < 6) throw InvalidLengthPasswordException()

        if (password != confirmPassword) throw InvalidConfirmPasswordException()

    }

}