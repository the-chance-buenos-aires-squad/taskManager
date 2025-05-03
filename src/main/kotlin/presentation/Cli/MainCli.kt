package presentation.Cli

import presentation.Cli.auth.LoginCli
import presentation.UiController

class MainCli(
    private val uiController: UiController,
    private val loginCli: LoginCli,
) {

    fun startCli() {
        while (true) {
            uiController.printMessage(
                "========================================\n" +
                        "              Welcome to PlanMate!       \n" +
                        "========================================\n" +
                        "1. Login\n" +
                        "2. Exit\n" +
                        "Choose an option (1 - 2): "
            )
            when (uiController.readInput().trim()) {

                "1" -> loginCli.start()
                "2" -> {
                    uiController.printMessage("Exiting PlanMate... Goodbye!")
                    break
                }

                else -> uiController.printMessage("Invalid option! Please try again.")


            }
        }
    }
}