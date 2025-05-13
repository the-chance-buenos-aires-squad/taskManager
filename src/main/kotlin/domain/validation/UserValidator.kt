package domain.validation

import presentation.exceptions.InvalidConfirmPasswordException
import presentation.exceptions.InvalidLengthPasswordException
import presentation.exceptions.PasswordEmptyException
import presentation.exceptions.UserNameEmptyException

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