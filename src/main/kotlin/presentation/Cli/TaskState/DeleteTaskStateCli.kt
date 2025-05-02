package presentation.Cli.TaskState

import domain.usecases.taskState.DeleteTaskStateUseCase
import presentation.UiController

class DeleteTaskStateCli(
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase,
    private val uiController: UiController
) {
    fun deleteTaskState() {
        uiController.printMessage("Enter task state ID to delete:")
        val taskId = uiController.readInput().trim()

        if (taskId.isEmpty()) {
            throw IllegalArgumentException("ID can't be empty")
        }

        val result = deleteTaskStateUseCase.execute(taskId)

        uiController.printMessage(
            if (result) "Task state deleted successfully."
            else "Failed to delete task state."
        )
    }
}