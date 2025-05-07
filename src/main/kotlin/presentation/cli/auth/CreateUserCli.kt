package presentation.cli.auth

import domain.usecases.CreateUserUseCase
import presentation.UiController


class CreateUserCli(
    private val uiController: UiController,
    private val createUserUseCase: CreateUserUseCase,
) {

    suspend fun start() {
        uiController.printMessage(HEADER_MESSAGE)

        uiController.printMessage(USERNAME_PROMPT_MESSAGE)
        val username = uiController.readInput().trim()

        uiController.printMessage(PASSWORD_PROMPT_MESSAGE)
        val password = uiController.readInput().trim()

        uiController.printMessage(CONFIRM_PASSWORD_PROMPT_MESSAGE)
        val confirmPassword = uiController.readInput().trim()
        try {
            val newUserMate = createUserUseCase.addUser(username, password, confirmPassword)
            uiController.printMessage(SUCCESS_MESSAGE.format(newUserMate.username))
        } catch (e: Exception) {
            uiController.printMessage(ERROR_MESSAGE.format(e.message))
        }

    }

    companion object {
        const val HEADER_MESSAGE = "========================================\n" +
                "              Create New User           \n" +
                "========================================\n"
        const val USERNAME_PROMPT_MESSAGE = "username: "
        const val PASSWORD_PROMPT_MESSAGE = "password: "
        const val CONFIRM_PASSWORD_PROMPT_MESSAGE = "confirm password: "
        const val SUCCESS_MESSAGE = "add new user mate %s successfully"
        const val ERROR_MESSAGE = "Error: %s"
    }

}