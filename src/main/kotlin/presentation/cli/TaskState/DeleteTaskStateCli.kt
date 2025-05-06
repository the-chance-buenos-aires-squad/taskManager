package presentation.cli.TaskState

import domain.customeExceptions.InvalidIdException
import domain.usecases.taskState.DeleteTaskStateUseCase
import presentation.UiController
import java.util.UUID

class DeleteTaskStateCli(
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase,
    private val uiController: UiController
) {
    fun deleteTaskState(projectId : UUID) {
        uiController.printMessage("Enter task state ID to delete:")
        val taskId = uiController.readInput().trim()

        if (taskId.isEmpty()) {
            throw InvalidIdException("ID can't be empty")
        }

        //todo show all tasks on project to select task

        val result = deleteTaskStateUseCase.execute(UUID.randomUUID())

        uiController.printMessage(
            if (result) "Task state deleted successfully."
            else "No task state found with this ID."
        )
    }
}