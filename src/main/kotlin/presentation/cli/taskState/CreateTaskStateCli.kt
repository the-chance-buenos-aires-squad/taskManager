package presentation.cli.taskState

import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.ExistsTaskStateUseCase
import presentation.UiController
import java.util.*

class CreateTaskStateCli(
    private val createTaskStateUseCase: CreateTaskStateUseCase,
    private val existsTaskStateUseCase: ExistsTaskStateUseCase,
    private val uiController: UiController,
) {
    private val inputHandler = TaskStateInputHandler(uiController)

    suspend fun createTaskState(selectedProject: UUID) {

        uiController.printMessage(
            """
            
            ===     Create Task State  📋   ===
            
            """.trimIndent()
        )

        val taskState = inputHandler.readAndValidateUserInputs(projectId = UUID.fromString("eddb0f91-0ea2-403c-8bfd-1617efa945c3"))

        if (existsTaskStateUseCase.execute(taskState.name, UUID.fromString("eddb0f91-0ea2-403c-8bfd-1617efa945c3"))) {
            uiController.printMessage("Task state already exists.")
            return
        }

        val newState = createTaskStateUseCase.execute(taskState.copy(projectId = UUID.fromString("eddb0f91-0ea2-403c-8bfd-1617efa945c3")))

        if (newState) {
            uiController.printMessage("Task state created successfully.")
            uiController.printMessage(
                """
                Name State: ${taskState.name}
                """.trimIndent()
            )
        } else uiController.printMessage("Failed to create task state.")
    }
}