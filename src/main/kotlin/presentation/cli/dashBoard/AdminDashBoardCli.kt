package presentation.cli.dashBoard

import presentation.UiController
import presentation.cli.GetAllAuditsCli
import presentation.cli.auth.CreateUserCli
import presentation.cli.project.ProjectScreenController
import presentation.cli.taskState.TaskStateCliController

class AdminDashBoardCli(
    private val uiController: UiController,
    private val createUserCli: CreateUserCli,
    private val projectScreenController: ProjectScreenController,
    private val taskStateCliController: TaskStateCliController,
    private val auditsCli: GetAllAuditsCli
) {
    fun start() {
        while (true) {
            uiController.printMessage(HEADER_ADMIN_DASHBOARD_MESSAGE)

            uiController.printMessage(CHOOSE_AN_OPTION_MESSAGE, true)

            when (uiController.readInput().toIntOrNull()) {
                1 -> createUserCli.start()
                2 -> projectScreenController.show()
                3 -> taskStateCliController.start()
                4 -> auditsCli.displayAllAudits()
                5 -> {
                    uiController.printMessage(LOGOUT_MESSAGE)
                    return
                }

                else -> uiController.printMessage(INVALID_OPTION_MESSAGE)
            }
        }
    }

    companion object {
        private const val HEADER_ADMIN_DASHBOARD_MESSAGE =
            " === Admin Dashboard ===\n" +
                    " 1. Create User Mate\n" +
                    " 2. Manage Project\n" +
                    " 3. Manage task States\n" +
                    " 4. View Audit Logs\n" +
                    " 5. Logout\n"
        private const val CHOOSE_AN_OPTION_MESSAGE = " Choose an option (1-5):"
        private const val INVALID_OPTION_MESSAGE = "Invalid option!"
        private const val LOGOUT_MESSAGE = "Logout"
    }

}