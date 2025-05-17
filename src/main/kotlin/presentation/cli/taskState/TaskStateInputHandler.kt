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
        val prefix = if (isEdit) NEW else EMPTY
        var name: String
        while (true) {
            uiController.printMessage(TASK_STATE_NAME.format(prefix))
            val input = uiController.readInput().trim()
            if (input.length < 2) {
                uiController.printMessage(TASK_STATE_NAME_WARNING.format(prefix))
            } else {
                name = input
                break
            }
        }
        val id = if (isEdit && existingId != null) existingId else UUID.randomUUID()
        return TaskState(id = id, title = name, projectId = projectId)
    }

    companion object Message {
        const val NEW = "New"
        const val EMPTY = ""
        const val TASK_STATE_NAME="Enter %s task state name:"
        const val TASK_STATE_NAME_WARNING="Warning: %s task state name must be at least 2 characters."
    }
}