package presentation.Cli.TaskState

import domain.Validator.TaskStateInputValidator
import domain.usecases.CreateTaskStateUseCase
import presentation.UiController

class CreateTaskStateCli(
    private val createTaskStateUseCase: CreateTaskStateUseCase,
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    private val inputHandler = TaskStateInputHandler(uiController, inputValidator)

    fun createTaskState() {
        val taskState = inputHandler.readAndValidateUserInputs()
        val result = createTaskStateUseCase.execute(taskState)

        uiController.printMessage(
            if (result) "Task state created successfully."
            else "Failed to create task state."
        )
    }
}