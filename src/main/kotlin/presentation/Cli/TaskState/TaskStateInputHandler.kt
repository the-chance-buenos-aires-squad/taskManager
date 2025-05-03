package presentation.Cli.TaskState

import domain.Validator.TaskStateInputValidator
import domain.entities.TaskState
import presentation.UiController

class TaskStateInputHandler(
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    fun readAndValidateUserInputs(isEdit: Boolean = false): TaskState {
        val prefix = if (isEdit) "New " else ""

        uiController.printMessage("Enter ${prefix}task state ID:")
        val id = uiController.readInput().trim()

        uiController.printMessage("Enter ${prefix}task state name:")
        val name = uiController.readInput().trim()

        uiController.printMessage("Enter ${prefix}project ID:")
        val projectId = uiController.readInput().trim()

        inputValidator.validate(id, name, projectId, isEdit)

        return TaskState(id = id, name = name, projectId = projectId)
    }
}