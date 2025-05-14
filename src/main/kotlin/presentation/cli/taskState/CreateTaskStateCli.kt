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

        uiController.printMessage(
            """
            =========  Create Task State  📋========
            """.trimIndent()
        )

        val taskState = inputHandler.readAndValidateUserInputs(projectId = projectId)

        try {
            val newState = createTaskStateUseCase.execute(taskState.copy(projectId = projectId))
            if (newState) {
                uiController.printMessage("Task state created successfully.")
                uiController.printMessage(
                    """
                Name State: ${taskState.title}
                """.trimIndent()
                )
            } else {
                uiController.printMessage("Failed to create task state.")
            }
        } catch (e: Exception) {
            uiController.printMessage("Error : ${e.localizedMessage}")
        }
    }
}