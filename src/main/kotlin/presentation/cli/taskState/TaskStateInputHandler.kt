package presentation.cli.taskState

import domain.entities.TaskState
import presentation.UiController
import java.util.*

class TaskStateInputHandler(
    private val uiController: UiController,
) {
    fun readAndValidateUserInputs(
        isEdit: Boolean = false,
        existingId: UUID? = null,
        projectId: UUID
    ): TaskState {
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
        val id = if (isEdit && existingId != null) existingId else UUID.randomUUID()
        return TaskState(id = id, name = name, projectId = projectId)
    }
}