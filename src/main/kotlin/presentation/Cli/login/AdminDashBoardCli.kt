package presentation.Cli.login

import presentation.Cli.CreateUserCli
import presentation.UiController

class AdminDashBoardCli(
    private val uiController: UiController,
    private val createUserCli: CreateUserCli
) {
    fun start() {
        while (true) {
            uiController.printMessage(

                " === Admin Dashboard ===\n" +
                        " 1. Create User Mate\n" +
                        " 2. Manage Project\n" +
                        " 3. Manage task States\n" +
                        " 4. View Audit Logs\n" +
                        " 5. Logout\n" +
                        " Choose an option (1-5):"

            )

            when (uiController.readInput()) {
                "1" -> createUserCli.start()
                "2" -> uiController.printMessage("Manage Project")
                "3" -> uiController.printMessage("Manage task States")
                "4" -> uiController.printMessage("View Audit Logs")
                "5" -> uiController.printMessage("Logout")
                else -> uiController.printMessage("Invalid option!")
            }
        }
    }
}