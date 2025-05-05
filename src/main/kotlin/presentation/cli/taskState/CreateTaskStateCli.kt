package presentation.cli.taskState

import TaskStateInputValidator
import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.ExistsTaskStateUseCase
import presentation.UiController

class CreateTaskStateCli(
    private val createTaskStateUseCase: CreateTaskStateUseCase,
    private val existsTaskStateUseCase: ExistsTaskStateUseCase,
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    private val inputHandler = TaskStateInputHandler(uiController, inputValidator)

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