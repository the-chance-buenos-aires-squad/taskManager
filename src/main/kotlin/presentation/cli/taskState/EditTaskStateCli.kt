package presentation.cli.taskState

import domain.usecases.taskState.EditTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import java.util.*

class EditTaskStateCli(
    private val editTaskStateUseCase: EditTaskStateUseCase,
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val uiController: UiController,
) {
    private val inputHandler = TaskStateInputHandler(uiController)

    suspend fun editTaskState(selectedProjectId: UUID) {
        uiController.printMessage(
            HEADER
        )

        val allTaskStates = getAllTaskStatesUseCase.execute(selectedProjectId)

        if (allTaskStates.isEmpty()) {
            uiController.printMessage(EMPTY_STATES)
            return
        }

        TaskStatePrinter.printAllTaskStates(allTaskStates, uiController)

        uiController.printMessage(INDEX_PROMPT)
        val input = uiController.readInput().trim()

        val index = input.toIntOrNull()?.minus(1)
        if (index == null || index !in allTaskStates.indices) {
            uiController.printMessage(INVALID_SELECTION)
            return
        }

        val selectedTaskState = allTaskStates[index]

        val updatedState = inputHandler.readAndValidateUserInputs(
            isEdit = true,
            existingId = selectedTaskState.id,
            projectId = selectedProjectId
        )

        val newStateEditing = editTaskStateUseCase.execute(updatedState)

        if (newStateEditing) {
            uiController.printMessage(SUCCESS)
            uiController.printMessage(
                """
                Updated Task State ${index + 1}: Name: ${updatedState.title}
                """.trimIndent()
            )
        } else uiController.printMessage(FAILURE)
    }
    companion object Messages {
        const val HEADER = "===   Edit Task State   ==="
        const val EMPTY_STATES = "No task states available to edit."
        const val INDEX_PROMPT = "Enter the number of the task state to edit:"
        const val INVALID_SELECTION = "Invalid selection."
        const val SUCCESS = "Task state edited successfully."
        const val FAILURE = "Failed to edit task state."
    }
}