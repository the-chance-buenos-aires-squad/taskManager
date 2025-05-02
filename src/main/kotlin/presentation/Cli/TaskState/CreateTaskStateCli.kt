package presentation.Cli.TaskState

import domain.Validator.TaskStateInputValidator
import domain.entities.TaskState
import domain.usecases.CreateTaskStateUseCase
import presentation.UiController

class CreateTaskStateCli(
    private val createTaskStateUseCase: CreateTaskStateUseCase,
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    fun createTaskState() {
        val taskState = readAndValidateUserInput()
        val result = createTaskStateUseCase.execute(taskState)

        uiController.printMessage(
            if (result) "Task state created successfully."
            else "Failed to create task state."
        )
    }


    private fun readAndValidateUserInput(): TaskState {
        uiController.printMessage("Enter task state ID:")
        val id = uiController.readInput().trim()

        uiController.printMessage("Enter task state name:")
        val name = uiController.readInput().trim()

        uiController.printMessage("Enter project ID:")
        val projectId = uiController.readInput().trim()

        inputValidator.validate(id, name, projectId)

        return TaskState(id = id, name = name, projectId = projectId)
    }
}