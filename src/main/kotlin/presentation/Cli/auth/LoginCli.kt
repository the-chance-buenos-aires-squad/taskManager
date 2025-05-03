package presentation.Cli.auth

import domain.entities.UserRole
import domain.usecases.AuthenticationUseCase
import presentation.Cli.dashBoard.AdminDashBoardCli
import presentation.Cli.dashBoard.MateDashBoardCli
import presentation.UiController

class LoginCli(
    private val uiController: UiController,
    private val authenticationUseCase: AuthenticationUseCase,
    private val adminDashBoardCli: AdminDashBoardCli,
    private val mateDashBoardCli: MateDashBoardCli
) {

    fun start(attempts: Int = 0) {
        if (attempts >= 2) {
            uiController.printMessage("Too many failed attempts. Returning to main menu.")
            return
        }

        uiController.printMessage("\n=== Login ===")

        uiController.printMessage("Username: ")
        val username = uiController.readInput().trim()

        uiController.printMessage("Password: ")
        val password = uiController.readInput().trim()

        try {
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
            start(attempts + 1)
        }
    }

}