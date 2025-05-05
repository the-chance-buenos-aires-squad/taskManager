package presentation.cli.taskState

import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.ExistsTaskStateUseCase
import presentation.UiController

class CreateTaskStateCli(
    private val createTaskStateUseCase: CreateTaskStateUseCase,
    private val existsTaskStateUseCase: ExistsTaskStateUseCase,
    private val uiController: UiController,
) {
    private val inputHandler = TaskStateInputHandler(uiController)

    fun createTaskState() {

        uiController.printMessage(
            """
            
            ***     Create Task State  📋   ***
            
            """.trimIndent()
        )

        val taskState = inputHandler.readAndValidateUserInputs()

        if (existsTaskStateUseCase.execute(taskState.name, taskState.projectId)) {
            uiController.printMessage("Task state already exists.")
            return
        }

        val newState = createTaskStateUseCase.execute(taskState)

        if (newState) {
            uiController.printMessage("Task state created successfully.")
            uiController.printMessage(
                """
                Name State: ${taskState.name}, Project ID: ${taskState.projectId}
                """.trimIndent()
            )
        } else {
            uiController.printMessage("Failed to create task state.")
        }
    }
}