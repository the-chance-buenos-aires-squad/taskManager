package presentation.cli.dashBoard

import presentation.UiController
import presentation.cli.task.ViewSwimlanesCLI

class MateDashBoardCli(
    private val uiController: UiController,
    private val viewSwimlanesCLI: ViewSwimlanesCLI
) {
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
                2 -> viewSwimlanesCLI.start()
                3 -> uiController.printMessage("View Audit Logs")
                4 -> uiController.printMessage("Logout")
                else -> uiController.printMessage("Invalid option!")
            }
        }
    }
}