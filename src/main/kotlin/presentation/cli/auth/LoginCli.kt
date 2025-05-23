package presentation.cli.auth

import domain.entities.UserRole
import domain.usecases.auth.LoginUseCase
import domain.validation.UserValidator
import presentation.UiController
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli
import presentation.exceptions.PasswordEmptyException
import presentation.exceptions.UserNameEmptyException

class LoginCli(
    private val uiController: UiController,
    private val authenticationUseCase: LoginUseCase,
    private val adminDashBoardCli: AdminDashBoardCli,
    private val mateDashBoardCli: MateDashBoardCli,
    private val userValidator: UserValidator
) {

    suspend fun start(loginAttempts: Int = INITIAL_ATTEMPT_COUNT) {
        if (loginAttempts >= MAX_ALLOWED_ATTEMPTS) {
            uiController.printMessage(MANY_FAILED_ATTEMPTS)
            return
        }

        uiController.printMessage(LOGIN)

        val username = validateUserNameInput() ?: return
        val password = validatePasswordInput() ?: return
        try {
            val validUser = authenticationUseCase.execute(username, password)
            uiController.printMessage(WELCOME.format(validUser.username))
            when (validUser.role) {
                UserRole.ADMIN -> {
                    adminDashBoardCli.start()
                }

                UserRole.MATE -> {
                    mateDashBoardCli.start()
                }
            }
        } catch (e: Exception) {
            uiController.printMessage(ERROR.format(e.message))
            start(loginAttempts + ATTEMPT_INCREMENT)
        }
    }

    private fun validatePasswordInput(): String? {
        repeat(2) { times: Int ->
            uiController.printMessage(PASSWORD)
            val password = uiController.readInput().trim()
            try {
                userValidator.validatePassword(password)
                return password
            } catch (e: PasswordEmptyException) {
                if (times < 1) {
                    uiController.printMessage("${e.message} $TRY_AGAIN")
                } else {
                    uiController.printMessage("${e.message}")
                }
            }
        }
        return null
    }

    private fun validateUserNameInput(): String? {
        repeat(2) { times: Int ->
            uiController.printMessage(USERNAME)
            val username = uiController.readInput().trim()
            try {
                userValidator.validateUsername(username)
                return username
            } catch (e: UserNameEmptyException) {
                if (times < 1) {
                    uiController.printMessage("${e.message} $TRY_AGAIN")
                } else {
                    uiController.printMessage("${e.message}")
                }
            }
        }
        return null
    }

    companion object {
        private const val INITIAL_ATTEMPT_COUNT = 0
        private const val ATTEMPT_INCREMENT = 1
        private const val MAX_ALLOWED_ATTEMPTS = 2
        const val MANY_FAILED_ATTEMPTS = "Too many failed attempts. Returning to main menu."
        const val LOGIN = "\n=== Login ==="
        const val WELCOME = "\nWelcome %s !"
        const val ERROR = "Error: %s"
        const val PASSWORD = "Password: "
        const val USERNAME = "Username: "
        const val TRY_AGAIN = "try again"
    }
}