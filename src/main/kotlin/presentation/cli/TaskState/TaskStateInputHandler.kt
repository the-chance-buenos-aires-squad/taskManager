package presentation.cli.TaskState

import TaskStateInputValidator
import domain.customeExceptions.InvalidTaskStateNameException
import domain.customeExceptions.InvalidProjectIdException
import domain.entities.TaskState
import presentation.UiController
import java.util.*

class TaskStateInputHandler(
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    fun readAndValidateUserInputs(isEdit: Boolean = false, existingId: UUID? = null): TaskState {
        val prefix = if (isEdit) "New " else ""

        var name: String
        while (true) {
            uiController.printMessage("Enter ${prefix}task state name:")
            val input = uiController.readInput().trim()
            if (input.length < 2) {
                uiController.printMessage("Warning: ${prefix}task state name must be at least 2 characters.")
            } else {
                name = input
                break
            }
        }

        var projectId: String
        while (true) {
            uiController.printMessage("Enter ${prefix}project ID:")
            val input = uiController.readInput().trim()
            if (!input.matches(Regex("^[Pp]\\d{2,}$"))) {
                uiController.printMessage("Warning: ${prefix}project ID must start with 'P' or 'p' followed by at least two digits.")
            } else {
                projectId = input
                break
            }
        }

        val id = if (isEdit && existingId != null) existingId else UUID.randomUUID()
        return TaskState(id = id, name = name, projectId = projectId)
    }
}