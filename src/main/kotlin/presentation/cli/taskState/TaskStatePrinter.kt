package presentation.cli.taskState

import domain.entities.TaskState
import presentation.UiController

object TaskStatePrinter {
    fun printAllTaskStates(taskStates: List<TaskState>, uiController: UiController) {
        uiController.printMessage(ALL_TASK_STATE)
        taskStates.forEachIndexed { index, taskState ->
            uiController.printMessage("Task State ${index + 1}: Name: ${taskState.title}")
        }
    }
        private const val ALL_TASK_STATE = "Here are all available task states:"
}