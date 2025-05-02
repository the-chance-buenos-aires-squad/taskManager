package presentation.Cli.login

import domain.usecases.AuthenticationUseCase
import presentation.UiController

class LoginCli(
    private val uiController: UiController,
    private val authenticationUseCase: AuthenticationUseCase,
    private val adminDashBoardCli: AdminDashBoardCli,
    private val mateDashBoardCli: MateDashBoardCli
) {

    fun start() {
    }

}