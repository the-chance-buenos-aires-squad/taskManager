package presentation.cli.TaskState

import TaskStateInputValidator
import domain.usecases.taskState.EditTaskStateUseCase
import presentation.UiController
import java.util.*

class EditTaskStateCli(
    private val editTaskStateUseCase: EditTaskStateUseCase,
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    private val inputHandler = TaskStateInputHandler(uiController, inputValidator)

    fun editTaskState(projectID: UUID) {
        val taskState = inputHandler.readAndValidateUserInputs(projectID,isEdit = true)
        val result = editTaskStateUseCase.execute(taskState)

        uiController.printMessage(
            if (result) "Task state edited successfully."
            else "No task states found."
        )
    }

}