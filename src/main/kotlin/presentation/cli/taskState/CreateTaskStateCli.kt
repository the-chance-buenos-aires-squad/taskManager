package presentation.cli.taskState

import domain.usecases.taskState.CreateTaskStateUseCase
import presentation.UiController
import java.util.*

class CreateTaskStateCli(
    private val createTaskStateUseCase: CreateTaskStateUseCase,
    private val uiController: UiController,
    private val inputHandler: TaskStateInputHandler
) {
    suspend fun createTaskState(projectId: UUID) {

        uiController.printMessage(HEADER)

        val taskState = inputHandler.readAndValidateUserInputs(projectId = projectId)

        try {
            val newState = createTaskStateUseCase.execute(taskState.copy(projectId = projectId))
            if (newState) {
                uiController.printMessage(SUCCESS)
                uiController.printMessage(NAME_STATE.format(taskState.title))
            } else {
                uiController.printMessage(FAILURE)
            }
        } catch (e: Exception) {
            uiController.printMessage(ERROR.format(e.localizedMessage))
        }
    }

    companion object Messages {
        const val HEADER = "=========  Create Task State  ========"
        const val SUCCESS = "Task state created successfully."
        const val FAILURE = "Failed to create task state."
        const val NAME_STATE = "Name State: %s"
        const val ERROR = "Error: %s"
    }
}