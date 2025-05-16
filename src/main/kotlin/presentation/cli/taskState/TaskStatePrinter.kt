package presentation.cli.taskState

import domain.entities.TaskState
import presentation.UiController

object TaskStatePrinter {
    fun printAllTaskStates(taskStates: List<TaskState>, uiController: UiController) {
        uiController.printMessage(ALL_TASK_STATE)
        taskStates.forEachIndexed { index, taskState ->
            uiController.printMessage(TASK_STATE_INDEX.format(index + 1) + TASK_STATE_NAME.format(taskState.title))
        }
    }

    private const val ALL_TASK_STATE = "Here are all available task states:"
    private const val TASK_STATE_NAME = ": Name: %s"
    private const val TASK_STATE_INDEX="Task State "
}