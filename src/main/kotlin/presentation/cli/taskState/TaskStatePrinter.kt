package presentation.cli.taskState

import domain.entities.TaskState
import presentation.UiController

object TaskStatePrinter {
    fun printAllTaskStates(taskStates: List<TaskState>, uiController: UiController) {
        uiController.printMessage("Here are all available task states:")
        taskStates.forEachIndexed { index, taskState ->
            uiController.printMessage("Task State ${index + 1}: Name: ${taskState.title}")
        }
    }
}