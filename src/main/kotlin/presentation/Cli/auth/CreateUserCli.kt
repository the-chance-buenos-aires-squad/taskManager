package presentation.Cli.auth

import domain.usecases.CreateUserUseCase
import presentation.UiController


class CreateUserCli(
    private val uiController: UiController,
    private val createUserUseCase: CreateUserUseCase
) {

    fun start() {}

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