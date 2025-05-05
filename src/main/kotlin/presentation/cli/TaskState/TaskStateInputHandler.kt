package presentation.cli.TaskState

import TaskStateInputValidator
import domain.entities.TaskState
import presentation.UiController
import java.util.UUID

class TaskStateInputHandler(
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    fun readAndValidateUserInputs(projectID: UUID, isEdit: Boolean = false): TaskState {
        val prefix = if (isEdit) "New " else ""

        uiController.printMessage("Enter ${prefix}task state ID:")
        val id = uiController.readInput().trim()

        uiController.printMessage("Enter ${prefix}task state name:")
        val name = uiController.readInput().trim()

        inputValidator.validate(id, name, projectID.toString(), isEdit)

        return TaskState(id=id, name = name, projectId = projectID.toString())
    }
}