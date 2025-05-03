package presentation.cli.dashBoard

import presentation.UiController

class MateDashBoardCli(private val uiController: UiController,) {
    fun start() {
        while (true) {
            uiController.printMessage(
                " === Mate Dashboard ===\n" +
                        " 1. Manage Task\n" +
                        " 2. View Swimlanes\n" +
                        " 3. Logout\n" +
                        " Choose an option (1-3):"
            )

            when (uiController.readInput().toIntOrNull()) {
                1 -> uiController.printMessage("Manage Task")
                2 -> uiController.printMessage("View Swimlanes")
                3 -> {
                    uiController.printMessage("Logout")
                    break
                }
                else -> uiController.printMessage("Invalid option!")
            }
        }
    }
}