package domain.validation

import data.exceptions.UserNameEmptyException
import domain.customeExceptions.InvalidConfirmPasswordException
import domain.customeExceptions.InvalidLengthPasswordException
import domain.customeExceptions.PasswordEmptyException

class UserValidator {

    //todo move it to Cli and use UIcontroller  to print user input error message

/*    fun validateUsername(username: String) {
        if (username.isBlank()) throw UserNameEmptyException()
    }*/

   /* private fun isPasswordEmpty(password: String) {
        if (password.isBlank()) throw PasswordEmptyException()
    }*/

   /* fun validatePassword(password: String, confirmPassword: String) {

        isPasswordEmpty(password)
        isPasswordEmpty(confirmPassword)

        if (password.length < 6) throw InvalidLengthPasswordException()

        if (password != confirmPassword) throw InvalidConfirmPasswordException()

    }*/

}