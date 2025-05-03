package presentation.cli.project

import domain.customeExceptions.NoProjectsFoundException
import domain.customeExceptions.UserEnterInvalidValueException
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
                } catch (exception: UserEnterInvalidValueException) {
                    uiController.printMessage(exception.message!!)
                }

                2 -> try {
                    updateProjectCli.update()
                } catch (exception: UserEnterInvalidValueException) {
                    uiController.printMessage(exception.message!!)
                } catch (exception: NoProjectsFoundException) {
                    uiController.printMessage(exception.message!!)
                }

                3 -> try {
                    deleteProjectCli.delete()
                } catch (exception: UserEnterInvalidValueException) {
                    uiController.printMessage(exception.message!!)
                } catch (exception: NoProjectsFoundException) {
                    uiController.printMessage(exception.message!!)
                }

                4 -> return
                null -> uiController.printMessage("Invalid Input should enter number.")
                else -> uiController.printMessage("Invalid Input should enter number in menu.")
            }
        }
    }
}