package presentation.Cli.TaskState

import presentation.UiController

class TaskStateCliController(
    private val taskStateShowMenu: TaskStateShowMenu,
    private val createTaskStateCli: CreateTaskStateCli,
    private val editTaskStateCli: EditTaskStateCli,
    private val deleteTaskStateCli: DeleteTaskStateCli,
    private val getAllTaskStatesCli: GetAllTaskStatesCli,
    private val uiController: UiController
) {
    fun show() {
        while (true) {
            taskStateShowMenu.showMenu()
            when (uiController.readInput().toIntOrNull()) {
                1 -> createTaskStateCli.createTaskState()
                2 -> editTaskStateCli.editTaskState()
                3 -> deleteTaskStateCli.deleteTaskState()
                4 -> getAllTaskStatesCli.getAllTaskStates()
                5 -> return
                null -> uiController.printMessage("Invalid input: please enter a number.")
                else -> uiController.printMessage("Invalid Input: please enter a number from the menu.")
            }
        }
    }
}