package presentation.cli.dashBoard

import presentation.UiController
import presentation.cli.task.ViewSwimlanesCLI

class MateDashBoardCli(
    private val uiController: UiController,
    private val viewSwimlanesCLI: ViewSwimlanesCLI
) {
    suspend fun start() {
        while (true) {
            uiController.printMessage(HEADER_MATE_DASHBOARD_MESSAGE)

            when (uiController.readInput().toIntOrNull()) {
                1 -> viewSwimlanesCLI.start()
                2 -> {
                    uiController.printMessage(LOGOUT_MESSAGE)
                    return
                }

                else -> uiController.printMessage(INVALID_OPTION_MESSAGE)
            }
        }
    }

    companion object {
        private const val HEADER_MATE_DASHBOARD_MESSAGE =
            " === Mate Dashboard ===\n" +
                    " 1. View Swimlanes\n" +
                    " 2. Logout\n" +
                    " Choose an option (1-2):"
        private const val INVALID_OPTION_MESSAGE = "Invalid option!"
        private const val LOGOUT_MESSAGE = "Logout"
    }
}