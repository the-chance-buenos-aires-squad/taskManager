package presentation.cli.auth

import domain.entities.UserRole
import domain.usecases.AuthenticationUseCase
import domain.validation.UserValidator
import presentation.UiController
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli

class LoginCli(
    private val uiController: UiController,
    private val authenticationUseCase: AuthenticationUseCase,
    private val adminDashBoardCli: AdminDashBoardCli,
    private val mateDashBoardCli: MateDashBoardCli,
    private val userValidator: UserValidator
) {

    suspend fun start(loginAttempts: Int = INITIAL_ATTEMPT_COUNT) {
        if (loginAttempts >= MAX_ALLOWED_ATTEMPTS) {
            uiController.printMessage("Too many failed attempts. Returning to main menu.")
            return
        }

        uiController.printMessage("\n=== Login ===")

        try {
            uiController.printMessage("Username: ")
            val username = uiController.readInput().trim()
            userValidator.validateUsername(username)

            uiController.printMessage("Password: ")
            val password = uiController.readInput().trim()
           userValidator.isPasswordEmpty(password)

            val validUser = authenticationUseCase.login(username, password)
            uiController.printMessage("\nWelcome ${validUser.username}!")
            when (validUser.role) {
                UserRole.ADMIN -> {
                    adminDashBoardCli.start()
                }

                UserRole.MATE -> {
                    mateDashBoardCli.start()
                }
            }
        } catch (e: Exception) {
            uiController.printMessage("Error: ${e.message}")
            start(loginAttempts + ATTEMPT_INCREMENT)
        }
    }

    companion object {
        private const val INITIAL_ATTEMPT_COUNT = 0
        private const val ATTEMPT_INCREMENT = 1
        private const val MAX_ALLOWED_ATTEMPTS = 2

    }
}