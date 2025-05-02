package domain.util

import domain.customeExceptions.InvalidConfirmPasswordException
import domain.customeExceptions.InvalidLengthPasswordException
import domain.customeExceptions.PasswordEmptyException
import domain.customeExceptions.UserNameEmptyException

class UserValidator {

    fun validateUsername(username: String) {
        if (username.isBlank()) throw UserNameEmptyException()
    }

    fun validatePassword(password: String, confirmPassword: String) {

        if (password.isBlank()) throw PasswordEmptyException()

        if (password.length < 6) throw InvalidLengthPasswordException()

        if (password != confirmPassword) throw InvalidConfirmPasswordException()

    }

}