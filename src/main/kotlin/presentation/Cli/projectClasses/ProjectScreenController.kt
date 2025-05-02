package presentation.Cli.projectClasses

import domain.customeExceptions.UserEnterEmptyValueException
import presentation.UiController

class ProjectScreenController(
    private val projectShowMenu: ProjectShowMenu,
    private val createProjectCli: CreateProjectCli,
    private val updateProjectCli: UpdateProjectCli,
    private val deleteProjectCli: DeleteProjectCli,
    private val uiController: UiController
) {
    fun show() {
        while (true) {
            projectShowMenu.showMenu()
            when (uiController.readInput().toIntOrNull()) {
                1 -> try {
                    createProjectCli.create()
                } catch (exception: UserEnterEmptyValueException) {
                    exception.message?.let { uiController.printMessage(it) }
                }

                2 -> try {
                    updateProjectCli.update()
                } catch (exception: UserEnterEmptyValueException) {
                    exception.message?.let { uiController.printMessage(it) }
                }

                3 -> deleteProjectCli.delete()
                4 -> return
                null -> uiController.printMessage("Invalid Input should enter number.")
                else -> uiController.printMessage("Invalid Input should enter number in menu.")
            }
        }
    }
}