package presentation.cli.dashBoard

import presentation.UiController

class MateDashBoardCli(private val uiController: UiController) {
    fun start() {
        while (true) {
            uiController.printMessage(
                " === Mate Dashboard ===\n" +
                        " 1. Manage Task\n" +
                        " 2. View Swimlanes\n" +
                        " 3. View Audit Logs\n" +
                        " 4. Logout\n" +
                        " Choose an option (1-4):"
            )

            when (uiController.readInput().trim()) {
                "1" -> uiController.printMessage("Manage Task")
                "2" -> uiController.printMessage("View Swimlanes")
                "3" -> uiController.printMessage("View Audit Logs")
                "4" -> uiController.printMessage("Logout")
                else -> uiController.printMessage("Invalid option!")
            }
        }
    }
}