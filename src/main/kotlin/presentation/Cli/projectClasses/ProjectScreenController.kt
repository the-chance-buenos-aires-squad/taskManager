package presentation.Cli.projectClasses

import presentation.UiController

class ProjectScreenController(
    private val projectShowMenu: ProjectShowMenu,
    private val projectActionHandler: ProjectActionHandler,
    private val uiController: UiController
) {
    fun show() {
        while (true) {
            when (projectShowMenu.showMenu()) {
                1 -> projectActionHandler.create()
                2 -> projectActionHandler.edit()
                3 -> projectActionHandler.delete()
                4 -> return
                null -> uiController.printMessage("Invalid Input should enter number.")
                else -> uiController.printMessage("Invalid Input should enter number in menu.")
            }
        }
    }
}