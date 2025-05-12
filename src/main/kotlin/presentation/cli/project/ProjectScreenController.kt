package presentation.cli.project

import presentation.UiController

class ProjectScreenController(
    private val projectShowMenu: ProjectShowMenu,
    private val createProjectCli: CreateProjectCli,
    private val updateProjectCli: UpdateProjectCli,
    private val deleteProjectCli: DeleteProjectCli,
    private val getAllProjectsCli: GetAllProjectsCli,
    private val uiController: UiController
) {
    suspend fun show() {
        while (true) {
            projectShowMenu.showMenu()
            when (uiController.readInput().toIntOrNull()) {
                1 -> createProjectCli.create()
                2 -> updateProjectCli.update()
                3 -> deleteProjectCli.delete()
                4 -> getAllProjectsCli.getAll()
                5 -> return
                null -> uiController.printMessage("Invalid Input should enter number.")
                else -> uiController.printMessage("Invalid Input should enter number in menu.")
            }
        }
    }
}