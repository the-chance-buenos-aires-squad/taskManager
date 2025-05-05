package presentation.cli.TaskState

import TaskStateInputValidator
import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.ExistsTaskStateUseCase
import presentation.UiController
import java.util.UUID

class CreateTaskStateCli(
    private val createTaskStateUseCase: CreateTaskStateUseCase,
    private val existsTaskStateUseCase: ExistsTaskStateUseCase,
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    private val inputHandler = TaskStateInputHandler(uiController, inputValidator)

    fun createTaskState(projectId: UUID) {
        val taskState = inputHandler.readAndValidateUserInputs(projectId)


        if (existsTaskStateUseCase.execute(taskState.id)) {
            uiController.printMessage("Task state already exists.")
            return
        }

        val result = createTaskStateUseCase.execute(taskState)

        uiController.printMessage(
            if (result) "Task state created successfully."
            else "Failed to create task state."
        )
    }
}