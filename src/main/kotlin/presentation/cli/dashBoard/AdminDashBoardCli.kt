package presentation.cli.dashBoard

import presentation.UiController
import presentation.cli.GetAllAuditsCli
import presentation.cli.TaskState.TaskStateCliController
import presentation.cli.auth.CreateUserCli
import presentation.cli.project.ProjectScreenController

class AdminDashBoardCli(
    private val uiController: UiController,
    private val createUserCli: CreateUserCli,
    private val projectScreenController: ProjectScreenController,
    private val taskStateCliController: TaskStateCliController,
    private val auditsCli: GetAllAuditsCli
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

            when (uiController.readInput().toIntOrNull()) {
                1 -> createUserCli.start()
                2 -> projectScreenController.show()
                3 -> taskStateCliController.show()
                4 -> auditsCli.displayAllAudits()
                5 -> {
                    uiController.printMessage("Logout")
                    break
                }

                else -> uiController.printMessage("Invalid option!")
            }
        }
    }
}