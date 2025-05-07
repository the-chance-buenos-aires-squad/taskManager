package presentation.cli.dashBoard

import presentation.UiController
import presentation.cli.task.ViewSwimlanesCLI

class MateDashBoardCli(
    private val uiController: UiController,
    private val viewSwimlanesCLI: ViewSwimlanesCLI
) {
    suspend fun start() {
        while (true) {
            uiController.printMessage(
                " === Mate Dashboard ===\n" +
                        " 1. View Swimlanes\n" +
                        " 2. Logout\n" +
                        " Choose an option (1-2):"
            )

            when (uiController.readInput().toIntOrNull()) {
                1 -> viewSwimlanesCLI.start()
                2 -> {
                    uiController.printMessage("Logout")
                    return
                }

                else -> uiController.printMessage("Invalid option!")
            }
        }
    }
}