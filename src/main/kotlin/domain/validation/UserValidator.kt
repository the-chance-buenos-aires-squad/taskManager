package domain.validation

import data.exceptions.UserNameEmptyException
import domain.customeExceptions.InvalidConfirmPasswordException
import domain.customeExceptions.InvalidLengthPasswordException
import domain.customeExceptions.PasswordEmptyException

class UserValidator {


  fun validateUsername(username: String) {
        if (username.isBlank()) throw UserNameEmptyException()
    }

    fun validatePassword(password: String) {
        if (password.isBlank()) throw PasswordEmptyException()
    }

    fun validatePasswordConfirmation(password: String, confirmPassword: String) {

        validatePassword(password)
        validatePassword(confirmPassword)

        if (password.length < 6) throw InvalidLengthPasswordException()

        if (password != confirmPassword) throw InvalidConfirmPasswordException()

    }

}